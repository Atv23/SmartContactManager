/**
 * 
 */
console.log("Script started");
// alert msg disappearance
document.addEventListener('DOMContentLoaded', function() {
	const errorMessage = document.getElementById('errorMessage');
	if (errorMessage) {
		setTimeout(function() {
			errorMessage.style.display = 'none';
		}, 5000);
	}
});


//side bar toggling
function closeSidebar() {
    const sidebar = document.getElementById("mySidebar");
	const content = document.querySelector(".content");
	
    sidebar.classList.add("closed"); // Add the 'closed' class to hide it
	content.style.marginLeft = "0%"; // Set content margin for closed sidebar
	
    setTimeout(() => {
        sidebar.style.display = "none"; // Set display to none after closing animation
    }, 500); // Adjust time to match CSS transition duration
}
function openSidebar() {
    const sidebar = document.getElementById("mySidebar");
	const content = document.querySelector(".content");
	
    // Ensure the sidebar is displayed
    sidebar.style.display = "block";
    
    // Remove the 'closed' class (if it exists) to open the sidebar
    sidebar.classList.remove("closed");
	content.style.marginLeft = "20%"; // Restore content margin when sidebar is open
}

//search contacts
const search = ()=> {
	console.log("searching contacts");
	let query= $("#search-contact").val();
	if(query=='')
	{
		$(".search-result").hide();
	}
	else{
		console.log(query);
        //sending req to server 
        let url = `/search/${query}`;
        fetch(url).then((response) => {
            return response.json();
        })
        .then((data)=>{
            //data
            let text=`<div class='list-group'>`;
            if (data.length === 0) {
                // If no contacts found, display a message
                text += `<p>No contacts found</p>`;
            } else {
                data.forEach((contact) => {
                    text += `<a href='/user/contact/${contact.contactId}' class='list-group-item contact-list list-group-action'> ${contact.contactName}  </a>`;
                });
            }
            text += `</div>`;
            $(".search-result").html(text);
            $(".search-result").show();
        });
	}
};

// Toggle Password Visibility
const togglePassword = document.getElementById('togglePassword');
const passwordField = document.getElementById('passwordField');
const passwordIcon = document.getElementById('passwordIcon');
const encodedMessage = document.getElementById('encodedMessage');
// Show message and reveal password when mouse is held down (mousedown)
togglePassword.addEventListener('mousedown', function () {
    encodedMessage.style.display = 'block'; // Show the message
    passwordField.type = 'text'; // Show the password
    passwordIcon.classList.remove('bi-eye-fill');
    passwordIcon.classList.add('bi-eye-slash-fill');
});
// Hide message and mask password when the mouse button is released (mouseup)
togglePassword.addEventListener('mouseup', function () {
    encodedMessage.style.display = 'none'; // Hide the message
    passwordField.type = 'password'; // Hide the password
    passwordIcon.classList.remove('bi-eye-slash-fill');
    passwordIcon.classList.add('bi-eye-fill');
});
// Also hide the message and reset the password when the mouse leaves the button (mouseleave)
togglePassword.addEventListener('mouseleave', function () {
    encodedMessage.style.display = 'none'; // Hide the message
    passwordField.type = 'password'; // Hide the password
    passwordIcon.classList.remove('bi-eye-slash-fill');
    passwordIcon.classList.add('bi-eye-fill');
});


console.log("page loaded");
//to search contacts

