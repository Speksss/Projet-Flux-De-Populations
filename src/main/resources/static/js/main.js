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

    //Formulaire de zone
    $("#creationForm").submit(function(event){
        // cancels the form submission
        event.preventDefault();
        if(creationValidation()){
            document.getElementById("creationForm").submit();
        }

    });

    function creationValidation(){
        var valid = true;

        if($("#name").val() == ""){
            $("#name").css("border-color","red");
            $("#name").next(".form-error").fadeIn().text("Veuillez entrer un nom !");
            valid=false;
        }

        if($("#capacity").val() == "" || isNaN($("#cp").val())){
            $("#capacity").css("border-color","red");
            $("#capacity").next(".form-error").fadeIn().text("Veuillez entrer une capacité !");
            valid=false;
        }

        if($("#listX").length == null || $("#listX").length < 4){
            $("#listX").next(".form-error").fadeIn().text("Veuillez choisir 4 points d'intérêt !");
            valid=false;
        }

        return valid;
    }
});