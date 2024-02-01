const msgRegister = document.getElementById('message-register');
const formRegister = document.getElementById('order-register');
const btnRegister = document.getElementById('btn-register');

const inputDateTime = document.getElementById('datetime');

btnRegister.addEventListener('click', clickedBtnRegister);

function clickedBtnRegister() {
    if (inputDateTime.value !== "") {
        const formDataRegister = new FormData(formRegister);
        const actionRegister = formRegister.getAttribute("action");
        const optionsRegister = {
            method: 'POST',
            body: formDataRegister,
        };
        console.log(actionRegister)
        fetch(actionRegister, optionsRegister).then((e) => {
            if (e.status === 200) {
                window.location.assign("/customer/list/1");
            } else {
                msgRegister.style.display = "initial";
            }
        });
    }
}
