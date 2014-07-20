var functionCalled = false;


function viewGroups() {
    if (!functionCalled) {
        functionCalled = true;
        createCourseMenu("#courses", "All courses");
    }
    var template;
    jQuery.get("template/iconTemplate", function (data) {
        template = data;
    });
    var output = "";
    var createGroupHtml="";
    var selectedStatus = $("#statuses").val();
    var selectedCourse = $("#courses").val();
    var statusUrl = "/groups/" + selectedStatus;
    if (selectedStatus == "PLANNED") {
        jQuery.get("template/createGroupHtml", function (data) {
            createGroupHtml = data;
        });
    }
    $.ajax({
//        url: statusUrl,
        url: "/groups/allGroups",
        dataType: "json",
        type: "GET",
        success: function (data) {
            var image;
            for (var index = 0; index < data.length; index++) {
                if ((data[index].groupStatus == selectedStatus || selectedStatus == "All groups" )
                    && (data[index].course.name == selectedCourse || selectedCourse == "All courses")) {
                    var view = {
                        ref: 'groups/list/' + data[index].groupID,
                        image: data[index].course.imageRef,
                        courseName: "Course : " + data[index].course.name,
                        groupName: "Group : " + data[index].groupName,
                    groupId: data[index].groupID
                }
                output += Mustache.render(template, view);
                }
            }
            output += createGroupHtml;
            $("#portfolio1").html(output);

        },
        error: function(data){
            console.log("" + data);
        }
    });


}

function viewDialog() {
    $("#dialog-form-add-group").data('content', 'Information updated!');
    $('#dialog-form-add-group').dialog('open');
}