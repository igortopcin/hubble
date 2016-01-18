requirejs.config({
	"baseUrl" : "/js/lib",
	"paths" : {
		"app" : "/js/app",
		"jquery" : "/js/jquery.min",
		"bootstrap" : "/js/lib/bootstrap.min"
	},
	shim : {
		"bootstrap" : {
			"deps" : [ 'jquery' ]
		}
	}
});

require([ 'jquery' ], function($) {
	module = $('script[src$="require.js"]').data('module');

	if (module) {
		require([ module ]);
	}
});
