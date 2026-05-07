(function (window, document) {
    var loadedScriptSrc = null;
    var currentOptions = null;

    function byId(id) {
        return id ? document.getElementById(id) : null;
    }

    function ensureTrailingSlash(path) {
        if (!path) {
            return "";
        }

        return path.charAt(path.length - 1) === "/" ? path : path + "/";
    }

    function setValue(id, value) {
        var element = byId(id);
        if (element) {
            element.value = value || "";
        }
    }

    function show(id) {
        var element = byId(id);
        if (element) {
            element.style.display = "";
        }
    }

    function hide(id) {
        var element = byId(id);
        if (element) {
            element.style.display = "none";
        }
    }

    function hasClass(element, className) {
        return (" " + element.className + " ").indexOf(" " + className + " ") !== -1;
    }

    function addClass(element, className) {
        if (element && !hasClass(element, className)) {
            element.className = element.className ? element.className + " " + className : className;
        }
    }

    function removeClass(element, className) {
        if (!element) {
            return;
        }

        element.className = (" " + element.className + " ").replace(" " + className + " ", " ").replace(/^\s+|\s+$/g, "");
    }

    function attachEvent(element, eventName, handler) {
        if (!element) {
            return;
        }

        if (element.addEventListener) {
            element.addEventListener(eventName, handler, false);
        } else if (element.attachEvent) {
            element.attachEvent("on" + eventName, handler);
        }
    }

    function setSubmitEnabled(config, enabled) {
        var button = byId(config.submitButtonId);
        if (!button) {
            return;
        }

        if (enabled) {
            removeClass(button, "disabled-button");
            button.disabled = false;
        } else {
            addClass(button, "disabled-button");
            button.disabled = true;
        }
    }

    function bindSubmit(config, state) {
        var button = byId(config.submitButtonId);
        var form = byId(config.formId);

        if (!button || !form || button._habboRegistrationBound) {
            return;
        }

        button._habboRegistrationBound = true;
        attachEvent(button, "click", function (event) {
            if (event && event.preventDefault) {
                event.preventDefault();
            } else {
                window.event.returnValue = false;
            }

            if (!state.allowedToProceed) {
                return false;
            }

            form.submit();
            return false;
        });
    }

    function setupBridge(config, state) {
        window.HabboEditor = {
            setGenderAndFigure: function (gender, figure) {
                state.gender = gender || "";
                state.figure = figure || "";

                setValue(config.genderInputId, state.gender);
                setValue(config.figureInputId, state.figure);
            },

            setGender: function (gender) {
                this.setGenderAndFigure(gender, state.figure);
            },

            setFigure: function (figure) {
                this.setGenderAndFigure(state.gender, figure);
            },

            setAllowedToProceed: function (allowed) {
                state.allowedToProceed = !!allowed;
                setSubmitEnabled(config, state.allowedToProceed);
            },

            setEditorState: function (editorState) {
                state.editorState = editorState || "";
                setValue(config.stateInputId, state.editorState);
            },

            showHabboClubNotice: function () {
                hide(config.oldFigureNoticeId);
                show(config.clubNoticeId);
                state.allowedToProceed = false;
                setSubmitEnabled(config, false);
            },

            hideHabboClubNotice: function () {
                hide(config.clubNoticeId);
            },

            showOldFigureNotice: function () {
                show(config.oldFigureNoticeId);
            }
        };
    }

    function loadScript(src, onReady) {
        if (window.HabboAvatarEditor) {
            onReady();
            return;
        }

        if (loadedScriptSrc === src) {
            attachEvent(document, "habboRegistrationEditorLoaded", onReady);
            return;
        }

        loadedScriptSrc = src;

        var script = document.createElement("script");
        script.type = "text/javascript";
        script.src = src;
        script.onload = function () {
            var event;
            if (document.createEvent) {
                event = document.createEvent("Event");
                event.initEvent("habboRegistrationEditorLoaded", true, true);
                document.dispatchEvent(event);
            }
            onReady();
        };

        document.getElementsByTagName("head")[0].appendChild(script);
    }

    window.HabboRegistration = {
        init: function (options) {
            var config = options || {};
            currentOptions = config;
            var basePath = ensureTrailingSlash(config.basePath || "/habbo-widgets/change-looks/");
            var target = byId(config.containerId);
            var state = {
                figure: config.figure || "",
                gender: config.gender || "",
                editorState: config.menuState || "",
                allowedToProceed: !config.submitButtonId
            };

            if (!target) {
                return;
            }

            target.innerHTML = "";
            var editorContainer = document.createElement("div");
            editorContainer.id = "editor-container";
            editorContainer.style.width = config.showRotationArrows ? "495px" : "435px";
            editorContainer.style.height = "400px";
            editorContainer.style.margin = "0 auto";
            editorContainer.style.backgroundColor = "#fff";
            target.appendChild(editorContainer);

            setupBridge(config, state);
            bindSubmit(config, state);
            setSubmitEnabled(config, state.allowedToProceed);

            setValue(config.genderInputId, state.gender);
            setValue(config.figureInputId, state.figure);
            setValue(config.stateInputId, state.editorState);

            if (config.showOnInit) {
                for (var i = 0; i < config.showOnInit.length; i++) {
                    show(config.showOnInit[i]);
                }
            }

            window.HabboEditorConfig = {
                figure: state.figure,
                gender: state.gender,
                userHasClub: !!config.userHasClub,
                showClubSelections: !!config.showClubSelections,
                showRotationArrows: !!config.showRotationArrows,
                assetsPath: basePath,
                assetBundlePath: config.assetBundlePath || "assets.zip",
                menuState: state.editorState,
                localization: config.localization || {
                    randomize: "Randomize",
                    boy: "Boy",
                    girl: "Girl"
                }
            };

            loadScript(basePath + (config.scriptName || "habbo-editor.iife.js"), function () {
                if (window.HabboAvatarEditor && editorContainer.childNodes.length === 0) {
                    new window.HabboAvatarEditor(editorContainer, window.HabboEditorConfig);
                }
            });
        },

        applyFigure: function (gender, figure) {
            if (!currentOptions) {
                return;
            }

            currentOptions.gender = gender || "";
            currentOptions.figure = figure || "";
            this.init(currentOptions);
        }
    };
}(window, document));
