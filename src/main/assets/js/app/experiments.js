requirejs(['domReady!', 'jquery', 'bootstrap'], function(doc, $) {
	var bindModalEvents = function() { 
		$('.btn-send-scan-to-galaxy').click(function(event) {
			var $button = $(event.target);
			var $loadingIcon = $button.find('span');
			
			var projectId = $button.data('project');
			var subjectId = $button.data('subject');
			var experimentId = $button.data('experiment');
			var scanId = $button.data('scan');
			
			console.log('Sending it to galaxy...');
			
			$loadingIcon.addClass('glyphicon glyphicon-refresh glyphicon-refresh-animate');

			$.get('/scans/sendToGalaxy?scanId=' + scanId + '&experimentId='+ experimentId + '&subjectId=' + subjectId + '&projectId=' + projectId, function(data) {
				$loadingIcon.removeClass('glyphicon glyphicon-refresh glyphicon-refresh-animate');
			});
		});
	};
	
	var $modal = $('#scans-modal');

	$('.btn-open-scans').click(function(event) {
		var $button = $(event.target);
		var $loadingIcon = $button.find('span');

		var projectId = $button.data('project');
		var subjectId = $button.data('subject');
		var experimentId = $button.data('experiment');

		$loadingIcon.addClass('glyphicon glyphicon-refresh glyphicon-refresh-animate');
		
		$.get('/scans?experimentId='+ experimentId + '&subjectId=' + subjectId + '&projectId=' + projectId, function(data) {
			$modal.find('.modal-body').html(data);
			$modal.modal('show');
			bindModalEvents();
			$loadingIcon.removeClass('glyphicon glyphicon-refresh glyphicon-refresh-animate');
		});
	});

});