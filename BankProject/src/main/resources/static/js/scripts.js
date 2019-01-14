var accounts;

function logout() {
    var params = {
        token_bank: getCookie("token_bank")
    }
    const http = new XMLHttpRequest();
    http.open('POST', '/logout');
    http.setRequestHeader('Content-type', 'application/json');
    http.send(JSON.stringify(params));
    // var result = null;
    http.onload = function() {
        window.location.href="home";
    }
}

function testAlert(){
    alert("Hello! I am an alert box!!");
}
function initClient() {
    console.log("cookie " + getCookie("token_bank"));

    //get data for table
    var params = {
        token_bank: getCookie("token_bank")
    }
    const http = new XMLHttpRequest();
    http.open('POST', '/accounts');
    http.setRequestHeader('Content-type', 'application/json');
    http.send(JSON.stringify(params));
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
            var headers = ["Номер счета","Баланс"];
            var fields = ["accountNumber","balance"];
            drawTable(result, headers, fields, 'tableDiv');
            drawDDList(result, 'selectDiv');
            drawDDList(result, 'selectDiv1');
        }
    }
    console.log("end get")
}

function drawTable(data, headers, fields, id){
    // get the reference for the body
    var div1 = document.getElementById(id);

    // creates a <table> element
    var tbl = document.createElement("table");
    tbl.setAttribute("class", "w3-table-all");

    //headers
    var headerRow = document.createElement("tr");
    for (var i = 0; i < headers.length; i++) {
        var cell = document.createElement("td");
        cell.appendChild(document.createTextNode(headers[i]));
        headerRow.appendChild(cell);
    }
    tbl.appendChild(headerRow);

    // creating rows
    for (var i = 0; i < data.length; i++) {
        var row = document.createElement("tr");

        for (var j = 0; j < headers.length; j++) {
            var cell1 = document.createElement("td");
            cell1.appendChild(document.createTextNode(data[i][fields[j]]));
            row.appendChild(cell1);
        }

        tbl.appendChild(row); // add the row to the end of the table body
    }
    div1.appendChild(tbl); // appends <table> into <div1>
}

function drawDDList(data, id){
    // get the reference for the body
    var div1 = document.getElementById(id);

    // creates a <select> element
    var select = document.createElement("select");
    select.setAttribute("class", "w3-select w3-border");
    select.setAttribute("id", "selectAccount");

    var defaultOption = document.createElement("option");
    defaultOption.appendChild(document.createTextNode("Выберите счет"));
    defaultOption.setAttribute("selected","selected");
    defaultOption.setAttribute("disabled","disabled");
    select.appendChild(defaultOption);

    // creating options
    for (var i = 0; i < data.length; i++) {
        var option = document.createElement("option");
        option.appendChild(document.createTextNode(data[i]["accountNumber"]));
        select.appendChild(option); // add the row to the end of the table body
    }
    div1.appendChild(select); // appends <table> into <div1>
}

function add(){
    var element = document.getElementById("selectAccount");
    var selectedValue = element.options[element.selectedIndex].text;
    console.log(selectedValue);
}

function openCity(evt, cityName) {
    var i, x, tablinks;
    x = document.getElementsByClassName("city");
    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablink");
    for (i = 0; i < x.length; i++) {
        tablinks[i].classList.remove("w3-light-grey");
    }
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.classList.add("w3-light-grey");
}

function processOperation(){
    document.getElementById('accountModal').style.display='none';
    alert("Успешно (пока ничего не происходит)")
}