// Place third party dependencies in the lib folder
//
// Configure loading modules from the lib directory,
// except 'app' ones, 
requirejs.config({
    "baseUrl": "/js/lib",
    "paths": {
      "app": "/js/app",
      "jquery": "/js/jquery.min",
	  "bootstrap": "/js/lib/bootstrap.min"
    },
	shim : {
	    "bootstrap" : { "deps" :['jquery'] }
	}
});

// Load the main app module to start the app
requirejs(["app/main"]);