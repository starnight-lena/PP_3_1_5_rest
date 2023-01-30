$(document).ready(function(){
    $('.table .deleteBtn').on('click',function(event){
        event.preventDefault();
        const href = $(this).attr('href');
        $.get(href, function (user, status) {
            $('.DeleteForm #userIdDelete').val(user.id);
            $('.DeleteForm #paramIdDelete').val(user.id);
            $('.DeleteForm #usernameDelete').val(user.username);
            $('.DeleteForm #firstNameDelete').val(user.firstName);
            $('.DeleteForm #lastNameDelete').val(user.lastName);
            $('.DeleteForm #ageDelete').val(user.age);
            $('.DeleteForm #emailDelete').val(user.email);
            var il = user.roles.length;
            $('.DeleteForm #role1Delete').text(user.roles[0].name);
            if (il > 1) {
                $('.DeleteForm #role2Delete').text(user.roles[1].name);
            }
        });
        $('.DeleteForm #deleteModal').modal();


    });

    $('.table .editBtn').on('click',function(event){
        event.preventDefault();
        const href2 = $(this).attr('href');
        $.get(href2, function (user, status) {
            $('.EditForm #userId').val(user.id);
            //$('.EditForm #paramIdEdit').val(user.id);
            var act = "admin/" + user.id + "/edit";
            $("#editForm").attr("action", act);
            $('.EditForm #username').val(user.username);
            $('.EditForm #firstName').val(user.firstName);
            $('.EditForm #lastName').val(user.lastName);
            $('.EditForm #age').val(user.age);
            $('.EditForm #email').val(user.email);
            $('.EditForm #ROLE_USER').prop('selected', false);
            $('.EditForm #ROLE_ADMIN').prop('selected', false);
            for (var i = 0; i < user.roles.length; i++) {
                if (user.roles[i].name =="ROLE_USER")  {
                    $('.EditForm #ROLE_USER').prop('selected', true);
                }
                else if (user.roles[i].name =="ROLE_ADMIN") {
                    $('.EditForm #ROLE_ADMIN').prop('selected', true);
                }
            }
        });
        $('.EditForm #editModal').modal();


    });

});