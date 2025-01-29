const stripe = Stripe('pk_test_51Qd1JfP4dz5QfHR5JzkRohUoYUry3Jrud0GV97bhlrmV9WrUUXf9VOpIAoMO4rukAHf82BAKUxAKtBGhRb57lHI600SicLXa2w');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });