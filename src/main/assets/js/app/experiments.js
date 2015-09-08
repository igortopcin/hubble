requirejs(['jquery', 'bootstrap'], function($) {
	$(function() {
		var $modal = $('#scans-modal');
		console.log('olar (onload)!');
		console.log($('#scans-modal'));

		$('.btn-open-scans').click(function(event) {
			console.log('olar (onclick)!');
			var $button = $(event.target);
			var $loadingIcon = $button.find('span');

			var projectId = $button.data('project');
			var subjectId = $button.data('subject');
			var experimentId = $button.data('experiment');
			
			$loadingIcon.addClass('glyphicon glyphicon-refresh glyphicon-refresh-animate');
			
			$.get('/scans?experimentId='+ experimentId + '&subjectId=' + subjectId + '&projectId=' + projectId, function(data) {
				$modal.find('.modal-body').html(data);
				$modal.modal('show');
				$loadingIcon.removeClass('glyphicon glyphicon-refresh glyphicon-refresh-animate');
			});
		});
	});
});