$(function () {
    window.fileUpload = function (file, success, error) {
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
            url: "/admin/common/fileupload",
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

    window.initCommonFileUploader = function (domId, isSingle) {
        if (isSingle !== true) {
            isSingle = false;
        }
        console.log("initFileUploader:" + domId);

        var obj = $('#' + domId),
            btn = obj.find(".up-btn"),
            updateBtn = obj.find(".uploader-update-btn"),
            img = obj.find(".img"),
            txt = obj.find(".status-bar"),
            hiddenInput = obj.find('input[type=hidden]'),
            fileInput = obj.find("input[type=file]");

        var urlArray = [];
        img.find('img').each(function () {
            var u = $(this).attr('data-url');
            u && urlArray.push(u);
        });
        console.log("init url array:" + urlArray);


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
            //btn.addClass("hidden");
            txt.removeClass('hidden').text(f.name + " 文件上传中，请稍等");
            var newImg = new Image();
            var reader = new FileReader();
            reader.onload = function () {
                newImg.src = this.result;
            };
            reader.onerror = function (e) {
                console.log(e);
            };
            reader.readAsDataURL(f);

            var newImgDom = $('<div></div>');
            newImgDom.append(newImg);
            btn.before(newImgDom);
        };

        var onUploadSuccess = function (res) {
            if (res.code === 0) {
                urlArray.push(res.msg);
                hiddenInput.val(urlArray.join(";"));
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
            fileUpload(file, onUploadSuccess, onUploadFail);
        });

        img.find('div[data-url]').on("mouseenter", function () {
            $(this).append($('#img-cover').html());
        }).on("mouseleave", function () {
            $(this).find('.img-cover').remove();
        });

        $(document).on('click', '#btnDel', function () {
            var target = $(this).parent().parent();
            var url = target.attr('data-url');
            urlArray.remove(url);
            hiddenInput.val(urlArray.join(";"));
            target.remove();
            console.log("delete:" + url);
        });
        $(document).on('click', '#btnCover', function () {
            var target = $(this).parent().parent();
            var url = target.attr('data-url');
            urlArray.remove(url);
            urlArray.unshift(url);
            hiddenInput.val(urlArray.join(";"));
            console.log("set cover:" + url);
            img.find('.cover').removeClass('cover');
            target.addClass("cover");
        });

    };


});