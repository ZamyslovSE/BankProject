function authenticate() {
    console.log("start auth")

    var params = {
        login: document.querySelector('#login').value,
        password: document.querySelector('#password').value
    }
    const http = new XMLHttpRequest()
    http.open("POST", "auth")
    http.setRequestHeader('Content-type', 'application/json')
    http.send(JSON.stringify(params))
    http.onerror = function() {
        if (this.status == 404) {
            <!--document.getElementById('404_msg').style.display='block'-->
            throw new Error("auth" + ' replied 404');
            console.log("response" + http.responseText)
        } else {
            console.log("else fail auth")
        }
    }
    http.onload = function() {
        if (this.status == 200) {
            console.log("success auth");
            var cookie_rnd = http.responseText;
            setCookie("token_bank", cookie_rnd, 1);
            console.log("set cookie " + cookie_rnd);
            window.location.href="client";
        }
    }
    console.log("end auth")
}

function chooseTab(tabName){
    var i, x;
    x = document.getElementsByClassName("pageTab");
    console.log(x.length);
    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }
    document.getElementById(tabName).style.display = "block";
}