define([ 'jquery' ], function($) {
	return {
		sendScan: function(project, subject, experiment, scan) {
			$.get("/scans/send", {
				projectId: project,
				subjectId: subject,
				experimentId: experiment,
				scanId: scan
			});
		}
	};
});
