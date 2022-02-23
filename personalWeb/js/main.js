$(document).ready(function () {
    function c(a) {
        $(".home").length && ($(document).scrollTop(), $("nav ul li a").each(function () {
            var b = $(this);
            $(b.attr("href"))
        }))
    }
    $(document).on("scroll", c);
    $('a[href^="#"]').on("click", function (a) {
        a.preventDefault();
        $(document).off("scroll");
        $("a").each(function () {
            $(this).removeClass("active");
            768 > $(window).width() && $(".nav-menu").slideUp()
        });
        $(this).addClass("active");
        var b = this.hash;
        b = $(b);
        $("html, body").stop().animate({
            scrollTop: b.offset().top - 80
        }, 500, "swing", function () {
            window.location.hash = b.selector;
            $(document).on("scroll", c)
        })
    });
    $(window).scroll(function () {
        200 < $(window).scrollTop() ? ($("#main-nav, #main-nav-subpage").slideDown(700), $("#main-nav-subpage").removeClass("subpage-nav")) : ($("#main-nav").slideUp(700), $("#main-nav-subpage").hide(), $("#main-nav-subpage").addClass("subpage-nav"))
    });
    $(".responsive").on("click", function (a) {
        $(".nav-menu").slideToggle()
    });
    var d = $(".typed");
    $(function () {
        d.typed({
            strings: ["陆姣姣.", "Lawyer.", "Hanfu Designer.", "Cat Owner"],
            typeSpeed: 100,
            loop: !0
        })
    });
    $(".owl-carousel").owlCarousel({
        autoplay: !0,
        loop: !0,
        margin: 50,
        dots: !0,
        nav: !1,
        responsiveClass: !0,
        responsive: {
            0: {
                items: 1
            },
            600: {
                items: 1
            },
            1E3: {
                items: 3
            }
        }
    });
    var e = $(".portfolio-container").isotope({
        itemSelector: ".portfolio-thumbnail",
        layoutMode: "fitRows"
    });
    $("#portfolio-flters li").on("click", function () {
        $("#portfolio-flters li").removeClass("filter-active");
        $(this).addClass("filter-active");
        e.isotope({
            filter: $(this).data("filter")
        })
    });

    // $("#submit").click(function () {
    //     var name = $("#name").val();
    //     var phone = $("#phone").val();
    //     var subject = $("#subject").val();
    //     var message = $("#message").val();
    //     $("#sendmessage").empty(); // To empty previous error/success message.
    //     if(name == ''){
    //         $("#name-vvalidation").append("请输入最少2个字符!");
    //         $("#name-vvalidation").css("display","block");
    //     }
    //     // Checking for blank fields.
    //     if (name == '' || phone == '' || subject == '' ||message=='') {
    //         alert("Please Fill Required Fields");
    //     } else {
    //         // Returns successful data submission message when the entered information is stored in database.
    //         $.post("contact_form.php", {
    //             name1: name,
    //             email1: email,
    //             message1: message,
    //             contact1: contact
    //         }, function (data) {
    //             $("#returnmessage").append(data); // Append returned message to message paragraph.
    //             if (data == "Your Query has been received, We will contact you soon.") {
    //                 $("#form")[0].reset(); // To reset form fields on success.
    //             }
    //         });
    //     }
    // });

    (function () {
        $(".popup-img").magnificPopup({
            type: "image",
            removalDelay: 300,
            mainClass: "mfp-with-zoom",
            gallery: {
                enabled: !0
            },
            zoom: {
                enabled: !0,
                duration: 300,
                easing: "ease-in-out",
                opener: function (a) {
                    return a.is("img") ? a : a.find("img")
                }
            }
        })
    })()
});
