$(function () {
    $("#gStartDate").datepicker();
    $("#gEndDate").datepicker();
    $("#gStartBoardingDate").datepicker();
    $("#gStartTime").timepicker();
    $('#gStartTime').timepicker({ 'step': 10 });
    $('#gStartTime').timepicker({ 'timeFormat': 'H:i' });
    $("#dialog-form-add-group").dialog({
        modal: true,
        autoOpen: false,
        width: 'auto',
        resizable: false,
        dialogClass: 'dialog',
        show: { effect: "fade", duration: 800 },
        hide: { effect: "fade", duration: 800 },
        open: createCourseMenu("#gCourse", "Select group course"),
        close: function () {
            $("#gName").val("");
            $("#gCapacity").val("");
            $("#gStartDate").val("");
            $("#gEndDate").val("");
            $("#gAddress").val("");
            $('#gStartTime').val("");
            $('#gStartBoardingDate').val("");
            $("#gCourse").val("Select group course");
        }
    });

    $("#cancelUButton").click(function (e) {
        e.preventDefault();
        $("#dialog-form-add-group").dialog("close");
    });

    $("#saveUButton").click( function(e){
        e.preventDefault();
        e.stopPropagation();
        if($("#userForm").valid()){
            var group = getGroupFromForm();
            sendGroup(group);
            $("#dialog-form-add-group").dialog("close");
        }
    });

    function getGroupFromForm() {
        var course = {};
        course.name = $("#gCourse").val();
        var group =  {};
        group.groupID = "1";
        group.groupName = $("#gName").val();
        group.address = $("#gAddress").val();
        group.capacity = $("#gCapacity").val();
        group.startBoardingTime = new Date($("#gStartBoardingDate").val()).getTime();
        group.startTime = new Date($("#gStartDate").val()).getTime() + $("#gStartTime").timepicker('getSecondsFromMidnight') * 60;
        group.endTime = new Date($("#gEndDate").val()).getTime();
        group.course = course;
        return group;
    };

    function sendGroup(group) {
        var jsonGroup = JSON.stringify(group);
        var success = true;
        $.ajax({
            url: location.origin + "/groups/addGroup",
            contentType: 'application/json',
            mimeType: 'application/json',
            dataType: "json",
            async: false,
            type: "POST",
            data: jsonGroup,
            success: function (data) {
                viewGroups();
            },
            error: function (data) {
                console.log("" + data);
            }
        });
        return success;
    };

    jQuery.validator.addMethod("greaterThanBoardingDate",
        function (value, element, params) {
            var week = 604800000;
            var startDate = new Date(value).getTime();
            var boardingDate = new Date($(params).val()).getTime();
            return boardingDate + week <= startDate;
        });

    jQuery.validator.addMethod("greaterThan",
        function (value, element, params) {
            if (!/Invalid|NaN/.test(new Date(value))) {
                return new Date(value) > new Date($(params).val());
            }

            return isNaN(value) && isNaN($(params).val())
                || (Number(value) > Number($(params).val()));
        });

    jQuery.validator.addMethod("greaterThanToday",
        function (value, element, params) {
            if (!/Invalid|NaN/.test(new Date(value))) {
                return new Date(value) > params;
            }
            return isNaN(value) && isNaN(params)
                || (Number(value) > Number(params));
        });

    jQuery.validator.addMethod("notDefaultValue", function (value) {
        return value != "Select group course";
    });

    $("#userForm").validate({
        rules: {
            groupName: {
                required: true,
                minlength: 3,
                maxlength: 40
            },
            groupAddress: {
                required: true,
                minlength: 8,
                maxlength: 40
            },
            groupCourse: {
                required: true,
                notDefaultValue: true
            },
            groupCapacity: {
                required: true,
                min : 1,
                number : true
            },
            groupStartBoardingDate: {
                required: true,
                date: true,
                greaterThanToday: new Date()
            },
            groupStartDate: {
                required: true,
                date: true,
                greaterThanToday: new Date(),
                greaterThanBoardingDate: "#gStartBoardingDate"
            },
            groupStartTime:{
                required:true
            },
            groupEndDate: {
                required: true,
                date: true,
                greaterThan: "#gStartDate"
            }
        },
        messages: {
            groupName: {
                required: "Group name is required",
                minlength: jQuery.validator.format("At least {0} characters is required"),
                maxlength: jQuery.validator.format("No more than {0} characters is allowed")
            },
            groupAddress: {
                required: "Group address is required.",
                minlength: jQuery.validator.format("At least {0} characters is required"),
                maxlength: jQuery.validator.format("No more than {0} characters is allowed")
            },
            groupCourse: {
                required: "Group course is required.",
                notDefaultValue: "Choose group course"
            },
            groupCapacity: {
                required: "Group capacity is required.",
                number : "Should be a correct number.",
                min: jQuery.validator.format("At least 1 student is required")
            },
            groupStartBoardingDate: {
                required: "Start boarding date is required",
                date: "Should be a correct date format",
                greaterThanToday: "Should be greater than today's date"

            },
            groupStartDate: {
                required: "Start date is required.",
                date: "Should be a correct date format.",
                greaterThanToday: "Should be greater than today's date",
                greaterThanBoardingDate: "Should be greater than boarding date not less than 1 week"
            },
            groupStartTime:{
                required : "Start time is required"
            },
            groupEndDate: {
                required: "End date is required.",
                date: "Should be a correct date.",
                greaterThan: "Must be greater than start date"
            }
        }
    });
});

function createCourseMenu(position, value) {
    $.ajax({
        url: location.origin + "/groups/courses",
        dataType: "json",
        type: "GET",
        async: false,
        success: function (data) {
            var output = "<option value='' id='defaultChoose'  selected></option>";
            var template = "<option value={{courseName}}>{{courseName}}</option>";
            for (var index = 0; index < data.length; index++) {
                var view = {
                    courseName: data[index].name
                }
                output += Mustache.render(template, view);
            }
            $(position).html(output);
            $("#defaultChoose").attr('value', value);
            $("#defaultChoose").html(value);
        },
        error: function (data) {
            console.log("" + data);
        }
    });
}

