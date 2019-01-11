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
function render() {
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
        result = JSON.parse(http.response);
        console.log("in: " + result);
        accounts = JSON.parse(http.response);
        drawTable(result);
        drawDDList(result, 'selectDiv');
        drawDDList(result, 'selectDiv1');
    }
    console.log("end get")
}

function drawTable(data){
    // get the reference for the body
    var div1 = document.getElementById('tableDiv');

    // creates a <table> element
    var tbl = document.createElement("table");
    tbl.setAttribute("class", "w3-table-all");

    //headers
    var headers = ["Номер счета","Баланс"];
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

        var cell1 = document.createElement("td");
        cell1.appendChild(document.createTextNode(data[i]["accountNumber"]));
        row.appendChild(cell1);
        var cell2 = document.createElement("td");
        cell2.appendChild(document.createTextNode(data[i]["balance"]));
        row.appendChild(cell2);

        // create cells in row
        //for (key in data[i]) {
        //    var cell = document.createElement("td");
        //    cell.appendChild(document.createTextNode(data[i][key]));
        //    row.appendChild(cell);
        //}

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