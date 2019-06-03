
function viewUsers() {

    var usersBody = $("#users_body");
    usersBody.empty();

    $.ajax({
        type: "GET",
        url: "/api/user",
        dataType: "json",
        success: function(data, status, jqXHR) {
            var users = data;
            users.forEach(function(user) {

                var roleString = [];
                user.roles.forEach(
                    function(role) {
                        roleString.push(role.rolename);
                    }
                    , roleString);
                //todo вынести в отдельный метод
                usersBody.append(// todo ``
                    "<tr>" +
                    "<th>" + user.id + "</th>" +
                    "<th>" + user.username + "</th>" +
                    "<th>" + user.login + "</th>" +
                    "<th><span>" + roleString.join(", ") + "</span></th>" +
                    "<th><button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#manageUserModal\" onclick=\"fillManageUserForm(" + user.id + ");\">Редактировать</button></th>" +
                    "<th><button type=\"button\" class=\"btn btn-primary\" onclick=\"deleteUser(" + user.id + ");\" class=\"btn btn-danger\">Удалить</a></th>" +
                    "</tr>"
                );
            });
        },
        error: function() {
            alert("Error ajax request");
        }

    });
}

function deleteUser(userId) {

    $.ajax({
        type: "DELETE",
        url: "/api/user/" + userId,
        dataType: "json",
        success: function(data, status, jqXHR) {
            viewUsers();
        },
        error: function() {
            alert("Error ajax request");
        }
    });
}

function fillRoles(selectedRoles) {

    if(selectedRoles == undefined){
        selectedRoles = [];
    }

    $.ajax({
        type: "GET",
        url: "/api/role",
        dataType: "json",
        dataPass: { selRoles : selectedRoles},
        success: function(roles, status, jqXHR) {

            var selectRoles = $("#user_roles");
            selectRoles.empty();

            var selectedRoles = this.dataPass.selRoles;
            for (i = 0; i < roles.length; i++) {

                var role = roles[i];

                var roleFound = false;
                for(j = 0; j < selectedRoles.length; j++) {
                    selectedRole = selectedRoles[j];
                    if (selectedRole.id === role.id) {
                        roleFound = true;
                        break;
                    }
                }
                selectRoles.append("<option value=\"" + role.id + "\" " + (roleFound ?  "selected=\"selected\"" : "") + ">" + role.rolename + "</option>");

            }
        },
        error: function() {
            alert("Error ajax request");
        }
    });
}

function fillManageUserForm(id) {

    if(id == undefined) {
        fillUserManagerFormFields("","", "", "");
        fillRoles();
        return ;
    }

    $.ajax({
        type: "GET",
        url: "/api/user/" + id.toString(),
        dataType: "json",
        success: function(user, status, jqXHR) {

            fillUserManagerFormFields(
                user.id,
                user.username,
                user.login,
                user.password
            );
            fillRoles(user.roles);
        },
        error: function() {
            alert("Error ajax request");
        }
    });
}

function fillUserManagerFormFields(id, username, login, password) {

    $("#user_id").val(id);
    $("#user_username").val(username);
    $("#user_login").val(login);
    $("#user_pass").val(password);
}
// security
// delete
function manageUser() {

    $.ajax({
        type: "PUT",
        url: "/api/user",
        dataType: 'json',
        contentType: 'application/json',
        processData: false,
        data: JSON.stringify({
            id : $("#user_id").val(),
            username : $("#user_username").val(),
            login : $("#user_login").val(),
            pass : $("#user_pass").val(),
            roles : $("#user_roles").val().map(function (role) {
                return Number(role);
            })
        }),
        success: function(data, status, jqXHR) {
            viewUsers();
        },
        error: function() {
            alert("Error ajax request");
        }
    });
}