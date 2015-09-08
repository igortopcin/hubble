requirejs(['jquery'], function($) {
	$('#scans-modal').on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget)
		
		var projectId = button.data('projectId')
		var subjectId = button.data('subjectId')
		var experimentId = button.data('experimentId')
		  
		var modal = $(this)
		  
		$.get('/scans?experimentId='+ experimentId + '&subjectId=' + subjectid + '&projectId=' + projectId, function(data) {
			modal.find('.modal-body').html(data);
		});
	});
});