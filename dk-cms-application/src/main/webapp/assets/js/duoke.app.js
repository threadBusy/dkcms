Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] === val) return i;
    }
    return -1;
};
Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};
$.extend({

    showLoading: function (msg) {
        $('.top-bar-loading').css({top: 0});
        $('.page-loading-txt').html(msg);
    },
    hideLoading: function () {
        $('.top-bar-loading').animate({top: '-50px'}, "slow", function () {
            $('.page-loading-txt').html('');
        });

    },

    setCookie: function (cname, cvalue) {
        var d = new Date();
        d.setTime(d.getTime() + (3600 * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + "; " + expires;
    },

    //获取cookie
    getCookie: function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1);
            if (c.indexOf(name) !== -1) return c.substring(name.length, c.length);
        }
        return "";
    },

    //清除cookie
    clearCookie: function (name) {
        setCookie(name, "", -1);
    },
    invoke: function (api, data, success, fail) {
        $.showLoading("数据请求中");
        $.post(api, data, function (res) {
            $.hideLoading();
            if (res.code === 0) {
                if (success) {
                    success(res);
                } else {
                    popupMsg('操作成功:' + res.msg);
                }
            } else {
                popupError("错误:" + res.msg);
                fail && fail(res);
            }
        });
    }
});
$(function () {


    /**
     * oldhash 是防止 onhashchange 被调用两次
     * @type {string}
     */
    window.oldhash = '';
    window.onhashchange = function () {
        if (location.hash === '' || location.hash === oldhash) {
            return;
        }
        window.oldhash = location.hash;
        var url = location.hash.substr(1);
        $.showLoading('加载中');
        $.ajax({
            type: "GET",
            url: url,
            data: {},
            dataType: "html",
            timeout: 10000,
            success: function (res) {
                $(".main-body-wrap").html(res);
                window.NDUO.init();
            },
            error: function () {
                popupError(url + '加载失败');
            },
            complete: function () {
                $.hideLoading();
            }
        });

    };

    var Menu = {
        open: function (self) {
            $('.dashboard-left-bar').removeClass('dashboard-left-bar-close');

            self.removeClass('icon-menu-right');
            self.addClass("icon-menu-left");
            $.setCookie("menu-status", "open");
        },
        close: function (self) {
            $('.dashboard-left-bar').addClass('dashboard-left-bar-close');
            self.removeClass('icon-menu-left');
            self.addClass("icon-menu-right");
            $.setCookie("menu-status", "close");
        },
        init: function () {
            var switchBtn = $('#menu-switch');
            if ($.getCookie("menu-status") === 'close') {
                Menu.close(switchBtn);
            }
            switchBtn.on('click', function () {
                var self = $(this);
                if (self.hasClass('icon-menu-left')) {
                    Menu.close(self);
                } else {
                    Menu.open(self);
                }
            });
        }
    };
    Menu.init();

    $('.menu-item-sub-item').click(function () {
        $('.menu-item-sub-item.s').removeClass('s');
        $(this).addClass('s');

        var html = '<div>首页</div>';
        html += '<div>' + $(this).parent().prev().find('.menu-item-title-center').text() + '</div>';
        html += '<div>' + $(this).text() + '</div>';
        $('.top-bar-left').html(html);
    });
    $('.menu-item-title').click(function () {
        var self = $(this);
        if (self.data('open') === true) {
            self.next().hide();
            self.removeClass("open");
        } else {
            self.next().show();
            self.addClass("open");
        }
        self.data('open', !self.data('open'));
    });

    if (location.hash === '') {
        location.hash = '/admin/dashboard/main';
    } else {
        var hash = location.hash;
        location.hash = '';
        location.hash = hash;
    }

    window.popupMsg = function (msg, cb) {
        var target = $('.fixed-popup-msg');
        target.text(msg);
        target.animate({top: '10px'}, 'normal', 'swing', function () {
            setTimeout(function () {
                target.animate({top: '-110px'}, 'normal', 'swing');
                cb && cb();
            }, 1000)
        })
    };

    window.popupError = function (msg) {
        var target = $('.fixed-popup-error');
        target.text(msg);
        target.animate({top: '10px'}, 'normal', 'swing', function () {
            setTimeout(function () {
                target.animate({top: '-110px'}, 'normal', 'swing');
            }, 1000)
        })

    };


    function toggleCheckbox(self) {
        if (self.hasClass('icon-checkbox')) {
            self.addClass("icon-checkbox-s");
            self.removeClass("icon-checkbox");
        } else {
            self.removeClass("icon-checkbox-s");
            self.addClass("icon-checkbox");
        }
    }

    window.getCheckboxIds = function () {
        var id = [];
        $('.icon-checkbox-s').each(function () {
            if ($(this).attr('data-id') === 'all') {
                return;
            }
            id.push($(this).attr('data-id'));
        });
        return id;
    };
    window.collectData = function (s) {
        var data = {};
        var error = false;
        $(s).find('input[type=text],input[type=hidden],input[type=number],textarea,select')
            .each(function () {
                var self = $(this);
                var name = self.attr('name');
                var require = self.data('require');
                if (name !== undefined) {
                    var value = self.val();
                    if ((require !== undefined) && (require !== '')) {
                        if (!value) {
                            popupError(require);
                            error = true;
                        }
                    }
                    data[name] = value;
                } else {
                    console.debug("collectData catch name == null ")
                }
            });

        console.debug("collectData result:");
        console.debug(data);
        if (error) {
            return false;
        }
        return data;
    };

    window.modal = function (id) {
        $('.fixed-modal-bg').show();
        var target = $('#' + id);
        target.show();
        target.find('.icon-close-btn').on('click', function () {
            $('.fixed-modal-bg').hide();
            target.hide();
        });
    };
    window.disModal = function (id) {
        $('.fixed-modal-bg').hide();
        $('#' + id).hide();
    };
    $(document).on('click', 'div[data-href],a[data-href],div[href]', function (event) {
        var self = $(this);
        var href = self.attr('data-href') || self.attr('href');
        if (href !== '') {
            location.hash = href;
        }
        event.preventDefault();
        event.stopPropagation();
        return false;
    });
    $(document).on('hover', 'div[title],a[title]', function (event) {
            console.log('a');
    });

    $(document).on('click', '.icon-checkbox,.icon-checkbox-s', function () {
        var self = $(this);
        if (self.attr('data-id') === 'all') {
            $('.icon-checkbox,.icon-checkbox-s').each(function () {
                toggleCheckbox($(this));
            });
            return;
        }
        toggleCheckbox(self);
    });


    // dk-auto-form
    $('.main-body-wrap').on('click', '.dk-auto-submit-btn', function () {
        var form = $('.dk-auto-form');
        if (form.length === 0) {
            console.error("dk-auto-form not found");
            return;
        }
        var action = form.data('action');
        var data = collectData('.dk-auto-form');

        if (data === false) {
            return false;
        }
        var successUrl = form.data('success');
        $.invoke(action, data, function () {
            popupMsg("发布成功", function () {
                if (successUrl) {
                    if (('#' + successUrl) === location.hash) {
                        location.reload();
                    } else {
                        location.hash = successUrl;
                    }
                }
            })
        });
    });

});