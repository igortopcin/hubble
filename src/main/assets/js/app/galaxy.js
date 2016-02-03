define(['domReady!', 'jquery', 'bootstrap'], function(doc, $) {
	var $totalSelected = $('#selected-total');
	
	var bindEvents = function(selector) {
		$(selector).click(function(event) {
			var $button = $(this);
			var $buttonIcon = $button.find('span.button-icon');
			var $buttonText = $button.find('span.button-text');
			
			var ref = $button.data('ref');
			var type = $button.data('type');
	
			$buttonIcon.addClass('glyphicon-refresh glyphicon-refresh-animate');
			$buttonText.text('Wait');
	
			$.ajax({
				url: '/galaxy/select?ref=' + ref + '&type=' + type,
				type: 'PUT',
				contentType: 'application/json',
				success: function(data) {
					$buttonIcon.removeClass('glyphicon-refresh glyphicon-refresh-animate');
					$buttonIcon.toggleClass('glyphicon-ok');
					$button.toggleClass('btn-success btn-primary');
					$totalSelected.text(data);
					
					var selected = $button.attr('data-selected') == 'true';
					$button.attr('data-selected', !selected);
					
					if (selected) {
						$buttonText.text($button.data('unselectedtext'));
					} else {
						$buttonText.text($button.data('selectedtext'));
					}
				},
				statusCode: {
					401: function() {
						window.location.reload();
					}
				}
			});
		});
	};
	
	var pollStatus = function() {
		var completed = false;
		setTimeout(function() {
			$.ajax({
				url: '/galaxy/upload-status',
				type: 'GET',
				contentType: 'application/json',
				success: function(data) {
					$('.btn-send-item-to-galaxy[data-loading=true]').each(function() {
						var $button = $(this);
						if (data.remainingRefs.indexOf($button.data('ref')) < 0) {
							var $buttonIcon = $button.find('span.button-icon');
							$buttonIcon.removeClass('glyphicon-refresh glyphicon-refresh-animate');
							$buttonIcon.addClass('glyphicon-ok');
							$button.attr('data-loading', false);
						}
					});
					
					if (!data.uploadInProgress) {
						var $button = $('.btn-send-all-to-galaxy');
						var $buttonIcon = $button.find('span.button-icon');
						var $buttonText = $button.find('span.button-text');
	
						$buttonIcon.removeClass('glyphicon-refresh glyphicon-remove glyphicon-refresh-animate');
						$buttonIcon.addClass('glyphicon-ok');
						$button.removeClass('btn-error btn-primary');
						$button.addClass('btn-success');
						$buttonText.text('Sent');
						
						var $remainingButtons = $('.btn-send-item-to-galaxy[data-loading=true]');
						var $remainingButtonsIcon = $remainingButtons.find('span.button-icon');
						$remainingButtons.removeAttr('disabled');
						$remainingButtons.removeClass('btn-success btn-primary');
						$remainingButtons.addClass('btn-error');
						$remainingButtonsIcon.removeClass('glyphicon-refresh glyphicon-refresh-animate');
						$remainingButtonsIcon.addClass('glyphicon-remove');
						
						completed = true;
					}
				},
				complete: function() {
					if (!completed) {
						pollStatus();
					}
				},
				timeout: 5000
			});
		}, 10000);
	};
	
	bindEvents('.btn-send-item-to-galaxy');
	
	$('.btn-send-all-to-galaxy').click(function(event) {
		var $button = $(this);
		var $buttonIcon = $button.find('span.button-icon');
		var $buttonText = $button.find('span.button-text');
		var historyId = $button.data('history');

		$buttonIcon.addClass('glyphicon-refresh glyphicon-refresh-animate');
		$buttonText.text('Processing');
		$button.attr("disabled", "disabled");
		
		var $selectButtons = $('.btn-send-item-to-galaxy[data-selected=true]');
		$selectButtons.attr("disabled", "disabled");
		$selectButtons.attr("data-loading", true);
		$selectButtons.find('span.button-icon').addClass('glyphicon-refresh glyphicon-refresh-animate');
		$selectButtons.find('span.button-text').text('Sending');

		$.ajax({
			url: '/galaxy/history/' + historyId + '/send',
			type: 'POST',
			contentType: 'application/json',
			success: function() {
				pollStatus();
			},
			error: function() {
				$buttonIcon.removeClass('glyphicon-refresh glyphicon-ok glyphicon-refresh-animate');
				$buttonIcon.addClass('glyphicon-remove');
				$button.removeClass('btn-success btn-primary');
				$button.addClass('btn-error');
				$buttonText.text('Error! Try again');
			}
		});
	});

	$('.history-item').click(function(event) {
		var $item = $(this);
		var $button = $('.btn-send-all-to-galaxy');
		var $historyName = $button.find('span.history-name');
		var historyId = $item.data('history');
		var historyName = $item.text();
		
		$button.data('history', historyId);
		$historyName.text(historyName);
	});
	
	return {
		bind: bindEvents
	};
});
