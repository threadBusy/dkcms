Array.prototype.remove = function (b) {
    var a = this.indexOf(b);
    if (a >= 0) {
        this.splice(a, 1);
        return true;
    }
    return false;
};

(function () {

    var ND = function () {

    };
    ND.prototype.popup = function (msg) {
        if (!msg) return;

        $('body').append('<div id="popup_box" style="display: none;"></div>');
        var target = $('#popup_box');
        target.addClass('popup_box');
        target.text(msg).slideDown('', function () {
            setTimeout(function () {
                target.slideUp('', function () {
                    target.remove();
                });
            }, 5000);
        });
    };

    ND.prototype.fileUpload = function (file, success, error) {
        if (!file) {
            console.error("file empty:" + file);
            return;
        }
        var formData = new FormData();
        formData.append("uploadFile", file); // 可以增加表单数据

        var callback = function (res, cb) {
            if (typeof res == 'string') {
                try {
                    var obj = JSON.parse(res);
                    if (typeof obj == 'object') {
                        cb && cb(obj);
                        return;
                    }
                } catch (e) {
                    console.log('error：' + res + ' not json string !!!' + e);
                }
            }
            cb && cb(res);

        };
        $.ajax({
            url: "/admin/common/fileUpload",
            type: "post",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            mimeType: "multipart/form-data",
            success: function (res) {
                callback(res, success)
            },
            error: function (res) {
                callback(res, error)
            }
        });
    };

    ND.prototype.initCmsCheckbox = function (id) {
        console.log("initCmsCheckbox:" + id);
        var self = $('#' + id);
        var input = self.find('input[type=hidden]');
        var isMultiple = (self.attr('data-multiple') === "true");
        if (!isMultiple) {
            var value = input.val();
            self.find('.cms-checkbox-option').each(function () {
                var option = $(this);
                if (option.attr('data-value') === value) {
                    option.addClass('s');
                }
                option.on("click", function () {
                    self.find('.s').removeClass('s');
                    $(this).addClass('s');
                    input.val($(this).attr('data-value'));
                })
            })
        } else {
            var valueArr = (input.val() || '').split('|');
            self.find('.cms-checkbox-option').each(function () {
                var option = $(this);
                if (valueArr.indexOf(option.attr('data-value')) > -1) {
                    option.addClass('s');
                }
                option.on("click", function () {
                    var v = $(this).attr('data-value');
                    if ($(this).hasClass("s")) {
                        $(this).removeClass('s');
                        valueArr.remove(v)
                    } else {
                        $(this).addClass('s');
                        valueArr.push(v);
                    }
                    input.val(valueArr.join(','));
                });

            });
        }

    };
    ND.prototype.initFileUploader = function (id) {
        console.log("initFileUploader:" + id);
        var obj = $('#' + id),
            btn = obj.find(".uploader-btn"),
            updateBtn = obj.find(".uploader-update-btn"),
            img = obj.find(".uploader-img"),
            txt = obj.find(".uploader-txt"),
            hiddenInput = obj.find('input[type=hidden]'),
            fileInput = obj.find("input[type=file]");


        var onInitImg = function (src) {
            img.removeClass('hidden');
            img.attr('src', src);
            img.attr('alt', src);
            btn.addClass("hidden");
            updateBtn.removeClass("hidden");
            txt.removeClass("hidden");
            txt.text(src);
        };
        var onUploadStart = function (f) {
            btn.addClass("hidden");
            img.removeClass('hidden');
            txt.removeClass('hidden').text(f.name + " 文件上传中，请稍等");

            var reader = new FileReader();
            reader.onload = function () {
                img.attr("src", this.result);
            };
            reader.readAsDataURL(f);
        };

        var onUploadSuccess = function (res) {
            if (res.code === 0) {
                hiddenInput.val(res.msg);
                txt.text('图片上传成功');
            } else {
                txt.text('图片上传失败:' + res.msg);
            }
        };
        var onUploadFail = function (res) {
            txt.text('图片上传遇到错误:' + res.msg);
        };

        var onBtnClick = function () {
            fileInput.trigger('click');
        };

        var oldValue = hiddenInput.val();
        if (oldValue) {
            onInitImg(oldValue);
        }
        updateBtn.on("click", onBtnClick);
        btn.on("click", onBtnClick);

        fileInput.on('change', function (e) {
            var file = e.target.files[0] || e.dataTransfer.files[0];
            if (!file) {
                alert('选择文件出错');
                return;
            }
            onUploadStart(file);
            NDUO.fileUpload(file, onUploadSuccess, onUploadFail);
        })

    };

    ND.prototype.initTagSelect = function (id) {
        console.log("initTagSelect:" + id);
        var self = $('#' + id);
        var valueInput = self.find('input[type=hidden]');

        if (valueInput.val() !== '') {
            var oldValue = valueInput.val().split(',');
            oldValue.forEach(function (id) {
                self.find('.common-tag-select-option[data-id=' + id + ']').addClass('s');
            });

        }
        console.log(oldValue);
        var refreshId = function () {
            var arr = [];
            self.find('.common-tag-select-option').each(function () {
                console.log($(this));
                if ($(this).hasClass('s'))
                    arr.push(parseInt($(this).data('id')));
            });
            return arr.join(',');
        }

        self.find('.common-tag-select-option').on("click", function () {
            var _this = $(this);
            if (_this.hasClass('s')) {
                _this.removeClass('s');
            } else {
                _this.addClass('s');
            }
            valueInput.val(refreshId())
        })

    }

    ND.prototype.init = function () {
        $('.dkcms-checkbox').each(function () {
            var id = $(this).attr('id');
            NDUO.initCmsCheckbox(id);
        });
        $('.dkcms-file-uploader').each(function () {
            var id = $(this).attr('id');
            NDUO.initFileUploader(id);
        });
        $('.common-tag-select').each(function () {
            var id = $(this).attr('id');
            NDUO.initTagSelect(id);
        });

    }
    window.NDUO = new ND();

})();
