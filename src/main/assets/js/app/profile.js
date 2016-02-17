requirejs(['domReady!', 'jquery', 'bootstrap'], function(doc, $) {
	$('#change-password-checkbox').click(function() {
		var command = this.checked ? 'show' : 'hide';
		$('#change-password-box').collapse(command);
	});
});
