$(function () {
    loadGroupCapacity();
    loadApplicantsIDListByStatus('PASSED');
    loadApplicantsIDListByStatus('NOT_PASSED');
    loadApplicantsByStatus('PASSED');
    loadApplicantsByStatus('NOT_PASSED');
    if (applicants.length == 0) {
        $('.scheduleView').button({disabled: true});
        $('.nocontent').toggle();
    } else {
        applicants.forEach(function (element) {
            loadInterviewResult(element)
        });
//interviewed
        var interviewedRendered = Mustache.render(pageTemplate, {'data': applicants, 'rank': true});
        var interviewedAccordion = $('.interviewed');
        interviewedAccordion.html(interviewedRendered);

        $(".interviewed").sortable({"axis": "y", items: ".applicantContainer", handle: ".accordion-section",
            create: function (event) {
                sort($(event.target).children()[0], applicants, true);
            }, stop: function (event, ui) {
                highlight(ui.item);
            }
        });
    }
    postRender();
});
loadInterviewResult = function (element) {
    $.ajax({
        async: false,
        url: '/interviews/' + element.appointmentID,
        dataType: 'json',
        type: 'GET',
        success: function (interview) {
            element['interview'] = calculateInterviewResult(interview);
        },
        error: function () {
            $("#dialog").data('content', "Failed to load result of the interview by id:" + element.appointmentID);
            $('#dialog').dialog('open');
        }
    });
};
calculateInterviewResult = function (interview) {
    var result = {};
    var totalPoints = 0;
    result['finalComment'] = interview['finalComment'];
    $(interview['questions']).each(function (index, value) {
        totalPoints += +value['mark'] * +value['weight'];
    });
    result['totalPoints'] = totalPoints;
    return result;
};
highlight = function (item) {
    var siblings = $(item).siblings("div.applicantContainer").addBack();
    $(siblings).each(function (index, value) {
        if (index < groupCapacity) {
            $(value).children("h3").addClass("ui-state-highlight");
        } else {
            $(value).children("h3").removeClass("ui-state-highlight");
        }
    });
};