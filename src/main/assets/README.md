# Assets Build System

Builds and moves assets (javascripts, css, etc) necessary for running the Hubble webapps.

You'll need Node and the Node Package Manager (npm): nodejs.org.

Once npm is installed, install the grunt task manager and it's command line into your global scope:

    npm install -g grunt grunt-cli

Next, from within this directory, install the local build dependencies:

    cd src/main/assets
    npm install

You're now ready to re-build the  scripts after modifying them.


# Rebuilding

There are two methods for rebuilding: a complete rebuild and automatic, partial rebuilds while you develop.

A complete rebuild can be done with the following (from the `src/main/assets` directory):

    grunt

This will:

1. download *bower* dependencies and place them into `static/js/lib` and `static/css`, accordingly; 
2. compress the files in `src/main/assets/js` and place them in `static/js`

# Changing Styles/CSS

The CSS and styling used by Hubble is also controlled from this directory. Hubble uses LESS, a superset of CSS that
compiles to CSS, for its styling. LESS files are kept in `src/main/assets/less`. Compiled CSS is in `static/css`.

Use grunt to recompile the LESS in into CSS (from the `src/main/assets` directory):

    grunt


# Grunt watch

Grunt can also do an automatic, partial rebuild of any files you change *as you develop* by:

1. opening a new terminal session
2. `cd client`
3. `grunt watch`

This starts a new grunt watch process that will monitor the files in `src/main/assets` for changes and copy and
pack them when they change.

You can stop the `grunt watch` task by pressing `Ctrl+C`. Note: you should also be able to background that task if you
prefer.


# Using a Locally Installed Version of Grunt

A non-global version of grunt and the grunt-cli are installed when using 'npm install'. If you'd rather build with that
version, you'll need to use the full, local path when calling it:

    ./node_modules/.bin/grunt
    # or
    ./node_modules/.bin/grunt watch
