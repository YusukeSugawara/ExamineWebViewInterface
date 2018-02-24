var elementHello = null;

window.onload = function() {
    document.body.insertAdjacentHTML("afterbegin", '<div id="hello"></div>');

    elementHello = document.getElementById("hello");
};

function hello(timeMillis) {
    if (elementHello == null) {
        return;
    }

    var date = new Date(timeMillis * 1000);
    elementHello.innerHTML = "hello: " + date.toISOString();
}
