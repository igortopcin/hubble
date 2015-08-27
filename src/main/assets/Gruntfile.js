module.exports = function(grunt) {
	grunt.file.defaultEncoding = 'utf-8';

	// Project configuration.
	grunt.initConfig({
		concat : {
			options : {
				separator : ';'
			},
			js : {
				files : {
					'../resources/static/js/app/main.js' : [ 'js/app/*.js' ],
					'../resources/static/js/app.js' : [ 'js/app.js' ]
				}
			}
		},

		jshint : {
			all : [ 'Gruntfile.js', 'js/**/*.js' ]
		},

		less : {
			dist : {
				options : {
					paths : [ "less" ]
				},
				files : [ {
					expand : true,
					flatten : true,
					src : [ 'less/**/*.less' ],
					dest : '../resources/static/css/',
					ext : '.css'
				} ]
			}
		},

		"bower-install-simple" : {
			options : {
				color : true,
			},
			"prod" : {
				options : {
					production : true
				}
			},
			"dev" : {
				options : {
					production : true
				}
			}
		},

		copy : {
			vendor_css : {
				expand : true,
				flatten : true,
				cwd : 'bower_components',
				src : [ 'bootstrap/dist/css/bootstrap.min.css' ],
				dest : '../resources/static/css/'
			},
			vendor_js : {
				expand : true,
				flatten : true,
				cwd : 'bower_components',
				src : [ '**/bootstrap.min.js', '**/requirejs.js' ],
				dest : '../resources/static/js/lib'
			},
			jquery : {
				expand : true,
				flatten : true,
				cwd : 'bower_components',
				src : [ '**/jquery.min.js' ],
				dest : '../resources/static/js/'
			}
		},

		notify : {
			watch : {
				options : {
					title : 'Watch task completed',
					message : 'Files generated'
				}
			}
		},

		watch : {
			js : {
				files : [ 'js/**/*.js' ],
				tasks : [ 'jshint', 'concat', 'notify' ]
			},
			style : {
				files : [ 'less/**/*.less' ],
				tasks : [ 'less', 'notify' ]
			}
		}
	});

	var dependencies = [ 'bower-install-simple', 'contrib-copy',
			'contrib-concat', 'contrib-jshint', 'contrib-less',
			'contrib-watch', 'notify' ];
	dependencies.forEach(function(dependency) {
		grunt.loadNpmTasks('grunt-' + dependency);
	});

	grunt.registerTask('bower', [ 'bower-install-simple', 'copy:vendor_css',
			'copy:vendor_js', 'copy:jquery' ]);
	grunt.registerTask('default', [ 'bower', 'jshint', 'less', 'concat' ]);

};