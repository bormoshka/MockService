/**
 * Ну и дичь...
 */
$(function () {
    var msgSuccess = $("#msgSuccess");
    var msgError = $("#msgError");
    $('#config_AutoChangeVal_' + config['autoChangeStatus']).find('.glyphicon').show();
    $('.configChange').click(function () {
        var newStatus = $(this).attr("data-value");
        console.log(newStatus);
        $.ajax(autoStatusChangeURL + newStatus + "/1")
            .done(function () {
                $('.configChange').each(function () {
                    $(this).find('.glyphicon').hide();

                });
                $('#config_AutoChangeVal_' + newStatus).find('.glyphicon').show();

                msgSuccess.find('.text').text("Конфигурация изменена успешно");
                msgSuccess.show();
                msgError.hide();
            })
            .fail(function () {
                msgError.find('.text').text("Ошибка изменения конфига");
                msgError.show();
                msgSuccess.hide();
            })
            .always(function () {

            });
    });

    changeItemStatus("#itemsNew", "#itemsProcessed");
    changeItemStatus("#itemsProcessed", "#itemsProcessed");

    colorTheTables();
});

function changeItemStatus(idFrom, idTo) {
    var msgError = $("#msgError");
    var insertTo = $(idTo);
    var onlyStatusChange = idFrom == idTo;
    $(idFrom).find('.contentLine').each(function () {
        var tr = $(this);
        console.log("--==FOUND TR==--");
        tr.find('a').each(function () {
            console.log("--==FOUND ANCHOR==--");
            $(this).on('click', function (event) {
                event.preventDefault();
                console.log("--==CLICK==--");
                var anchor = $(this);
                if (anchor.hasClass('inProgress')) {
                    return;
                }
                var onlyRemove = anchor.hasClass('btn-delete');
                onlyStatusChange &= !onlyRemove;
                anchor.addClass('inProgress');
                var href = anchor.attr('href');
                $.ajax({url: href, type: 'POST'})
                    .done(function (data) {
                        console.log(data);

                        if (!onlyStatusChange) {
                            tr.slideUp(250, function () {
                               // tr.remove(); // or not?
                                if (!onlyRemove) {
                                    insertTo.append(tr);
                                    tr.slideDown(200);
                                    tr.find('a').each(function() {
                                       if (!$(this).hasClass('btn-delete')) {
                                           if ($(this).attr('class') != anchor.attr('class')) {
                                               $(this).remove();
                                               colorTheTables();
                                           }
                                       }
                                    });
                                }
                            });
                        } else {
                            if (href.substr(href.length - 5, href.length) == 'false') {
                                anchor.attr('href', href.substr(0, href.length - 5) + true);
                            } else {
                                anchor.attr('href', href.substr(0, href.length - 4) + false);
                            }
                            anchor.toggleClass('btn-success')
                                .toggleClass('btn-warning')
                                .find('span').toggleClass('glyphicon-thumbs-down')
                                .toggleClass('glyphicon-thumbs-up');

                            changeTableLine(data.model);
                        }
                    })
                    .fail(function (data) {
                        msgError.find('.text').text("Ошибка операции: " + JSON.stringify(data));
                        msgError.show();
                    })
                    .always(function () {
                        anchor.removeClass('inProgress');
                    });
            });
        });
    });
}

function changeTableLine(json) {
    var tr = $('#line_' + json.msid);
    tr.find('.status').text(json.status);
    tr.find('.touchCount').text(json.touchCount);
    tr.find('.infoCol').text(json.info);
    colorTheTables();
}

function colorTheTables() {
    colorTheTable("#itemsNew");
    colorTheTable("#itemsProcessed");
}

function colorTheTable(table) {
    $(table).find('tr').each(function() {
        var tr = $(this);
        var status = tr.find('.status').text();
        tr.removeClass("warning").removeClass('success');
        switch(status) {
            case'notdelivered':
            case'notsent': {
                tr.addClass("warning");
                break;
            }
            case 'delivered':
            case 'sent': {
                tr.addClass('success');
                break;
            }
        }
    });
}