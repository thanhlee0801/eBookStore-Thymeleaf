function addToCart(bookId, quantity) {
    fetch(`/user/cart/add/${bookId}?quantity=${quantity}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Success:', data);
            alert('Đã thêm sản phẩm vào giỏ hàng!');
            // Reload page để cập nhật UI
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi thêm vào giỏ hàng!');
        });
}

function removeFromCart(bookId) {
    fetch(`/user/cart/remove/${bookId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Success:', data);
            // Reload page để cập nhật UI
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi xóa khỏi giỏ hàng!');
        });
}

function updateQuantity(bookId, quantity) {
    if (typeof quantity === 'string') {
        quantity = parseInt(quantity);
    }

    const currentInput = document.querySelector(`input[data-book-id="${bookId}"]`);
    let newQuantity;

    if (currentInput) {
        const currentQuantity = parseInt(currentInput.value);
        newQuantity = typeof quantity === 'number' && !isNaN(quantity) ?
            (quantity < 0 ? currentQuantity + quantity : quantity) :
            currentQuantity;

        if (newQuantity < 1) newQuantity = 1;
    } else {
        newQuantity = Math.max(1, quantity);
    }

    fetch(`/user/cart/update/${bookId}?quantity=${newQuantity}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Success:', data);
            // Reload page để cập nhật UI
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi cập nhật giỏ hàng!');
        });
}

function clearCart() {
    if (confirm('Bạn có chắc chắn muốn xóa tất cả sản phẩm khỏi giỏ hàng?')) {
        fetch('/user/cart/clear', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(() => {
            window.location.reload();
        })
        .catch(error => console.error('Error:', error));
    }
}