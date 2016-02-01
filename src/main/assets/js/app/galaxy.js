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
					
					var selected = !$button.data('selected');
					$button.data('selected', selected);
					
					if (selected) {
						$buttonText.text($button.data('selectedtext'));
					} else {
						$buttonText.text($button.data('unselectedtext'));
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
	
	bindEvents('.btn-send-item-to-galaxy');
	
	return {
		bind: bindEvents
	};
});
