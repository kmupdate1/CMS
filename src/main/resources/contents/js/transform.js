const register = document.getElementById('register');
const fileRegister = document.getElementById('file-register');
const registerLink = document.getElementById('register-link');
const fileRegisterLink = document.getElementById('file-register-link');

registerLink.addEventListener('click', () => {
    register.style.display = 'initial';
    fileRegister.style.display = 'none';
});
fileRegisterLink.addEventListener('click', () => {
    register.style.display = 'none';
    fileRegister.style.display = 'initial';
});
