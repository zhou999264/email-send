jQuery(document).ready(function (c) {
    c("form.contactForm").submit(function () {
        var h = c(this).find(".form-group"),
            f = !1,
            k = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
        h.children("input").each(function () {
            var b = c(this),
                a = b.attr("data-rule");
            if (void 0 !== a) {
                var d = !1,
                    e = a.indexOf(":", 0);
                if (0 <= e) {
                    var g = a.substr(e + 1, a.length);
                    a = a.substr(0, e)
                } else a = a.substr(e + 1, a.length);
                switch (a) {
                    case "required":
                        "" === b.val() && (f = d = !0);
                        break;
                    case "minlen":
                        b.val().length < parseInt(g) && (f = d = !0);
                        break;
                    case "phone":
                        k.test(b.val()) || (f = d = !0);
                        break;
                    case "checked":
                        b.is(":checked") || (f = d = !0);
                        break;
                    case "regexp":
                        g = new RegExp(g), g.test(b.val()) || (f = d = !0)
                }
                b.next(".validation").html(d ? void 0 !== b.attr("data-msg") ? b.attr("data-msg") : "wrong Input" : "").show("blind")
            }
        });
        h.children("textarea").each(function () {
            var b = c(this),
                a = b.attr("data-rule");
            if (void 0 !== a) {
                var d = !1,
                    e = a.indexOf(":", 0);
                if (0 <= e) {
                    var g = a.substr(e + 1, a.length);
                    a = a.substr(0, e)
                } else a = a.substr(e + 1, a.length);
                switch (a) {
                    case "required":
                        "" === b.val() && (f = d = !0);
                        break;
                    case "minlen":
                        b.val().length < parseInt(g) && (f = d = !0)
                }
                b.next(".validation").html(d ? void 0 != b.attr("data-msg") ? b.attr("data-msg") : "wrong Input" : "").show("blind")
            }
        });
        if (f) return !1;
        h = c(this).serialize();
        var params = h.split('&');
        var arrV = [];
        $.each(params, function (i, val) {
            var param = val.split('=');
            arrV.push(param[1]);
        });

        var nowTime = new Date().getTime();
        var clickTime = $(this).attr("ctime");
        if (clickTime != 'undefined' && (nowTime - clickTime < 10000)) {
            alert('操作过于频繁，稍后再试');
            return false;
        } else {
            $(this).attr("ctime", nowTime);
            c.ajax({
                type: "POST",
                url: "https://www.eshicha.cn/apis/sendMessage",
                data: { name: decodeURI(arrV[0]), phone: arrV[1], subject: arrV[2], message: arrV[3] },
                success: function (b) {
                    "200" == b.code ? alert('您的信息已经提交成功，谢谢！') : alert(b.message);
                }
            });
        }
        return !1
    })
});