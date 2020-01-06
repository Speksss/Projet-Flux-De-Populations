$(document).ready(function(){

    //Formulaire de login
    $("#loginForm").submit(function(event){
        // cancels the form submission
        event.preventDefault();
        if(loginValidation()){
            document.getElementById("loginForm").submit();
        }

    });

    function loginValidation(){
        var valid = true;

        //vérification adresse mail
        var regex = /^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\.[a-z]{2,4}$/;

        if($("#email").val() == "" || !regex.test($("#email").val())){
            $("#email").css("border-color","red");
            $("#email").next(".form-error").fadeIn().text("Veuillez entrer adresse mail !");
            valid=false;
        }

        if($("#password").val() == ""){
            $("#password").css("border-color","red");
            $("#password").next(".form-error").fadeIn().text("Veuillez entrer un mot de passe !");
            valid=false;
        }

        return valid;
    }
});