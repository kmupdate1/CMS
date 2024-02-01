const signing = document.getElementById('signing');
const register = document.getElementById('register');
const registerLink = document.getElementById('register-link');
const signingLink = document.getElementById('signing-link');

registerLink.addEventListener('click', () => {
    signing.style.display = 'none';
    register.style.display = 'initial';
});
signingLink.addEventListener('click', () => {
    signing.style.display = 'initial';
    register.style.display = 'none';
});
