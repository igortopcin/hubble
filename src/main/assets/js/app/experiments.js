requirejs(['domReady!', 'app/galaxy', 'jquery', 'bootstrap'], function(doc, galaxy, $) {
	galaxy.bind('.btn-send-experiment-to-galaxy');
	
	var $modal = $('#scans-modal');

	$('.btn-open-scans').click(function(event) {
		var $button = $(event.target);
		var $loadingIcon = $button.find('span');

		var experimentRef = $button.data('experiment');

		$loadingIcon.addClass('glyphicon glyphicon-refresh glyphicon-refresh-animate');
		
		$.get('/scans?experimentRef='+ experimentRef, function(data) {
			$modal.find('.modal-body').html(data);
			$modal.modal('show');
			galaxy.bind('.btn-send-scan-to-galaxy');
			$loadingIcon.removeClass('glyphicon glyphicon-refresh glyphicon-refresh-animate');
		});
	});

});
