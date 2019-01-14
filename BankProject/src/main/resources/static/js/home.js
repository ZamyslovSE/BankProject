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
    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }
    document.getElementById(tabName).style.display = "block";
}

function initHome() {
//    console.log("cookie " + getCookie("token_bank"));

    //get data for table
//    var params = {
//        token_bank: getCookie("token_bank")
//    }
    const http = new XMLHttpRequest();
    http.open('POST', '/products');
    http.setRequestHeader('Content-type', 'application/json');
//    http.send(JSON.stringify(params));
    http.send("dummy");
    var result = null;
    http.onload = function() {
        console.log("start onload");
        if (http.status === 403) {
            console.log("onload 403");
            window.location.href="home";
        }

        if (http.status === 404) {
            console.log("onload 404");
            window.location.href="home";
        }

        if (http.status === 202) {
            console.log("onload 202");
            result = JSON.parse(http.response);
            console.log("in: " + result);
            accounts = JSON.parse(http.response);
            var headers = ["Название","Проценты","Условия"];
            var fields = ["name","percent","description"];
            drawTable(result, headers, fields, "DEPOSIT", 'depositTable');

            var headers = ["Название","Стоимость","Условия"];
            var fields = ["name","fee","description"];
            drawTable(result, headers, fields, "DEBIT_CARD", 'debitTable');

            var headers = ["Название","Проценты","Условия"];
            var fields = ["name","percent","description"];
            drawTable(result, headers, fields, "ACCOUNT", 'accountTable');
        }
    }
}

function drawTable(data, headers, fields, type, id){
    // get the reference for the body
    var div1 = document.getElementById(id);

    // creates a <table> element
    var tbl = document.createElement("table");
    tbl.setAttribute("class", "w3-table w3-bordered");

    //headers
    var headerRow = document.createElement("tr");
    for (var i = 0; i < headers.length; i++) {
        var cell = document.createElement("th");
        cell.appendChild(document.createTextNode(headers[i]));
        headerRow.appendChild(cell);
    }
    tbl.appendChild(headerRow);

    // creating rows
    for (var i = 0; i < data.length; i++) {

        if (type === data[i]["type"]) {
            var row = document.createElement("tr");

            for (var j = 0; j < headers.length; j++) {
                var cell1 = document.createElement("td");
                if (fields[j] === "description") {
                    cell1.innerHTML = data[i][fields[j]]
                } else {
                    cell1.appendChild(document.createTextNode(data[i][fields[j]]));
                }
                row.appendChild(cell1);
            }

            tbl.appendChild(row); // add the row to the end of the table body
        }
    }
    div1.appendChild(tbl); // appends <table> into <div1>
}