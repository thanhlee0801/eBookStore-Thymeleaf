function toggleWishlist(bookId) {
    fetch(`/user/wishlist/toggle/${bookId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            // Refresh trang sau khi toggle wishlist thành công
            window.location.reload();
        })
        .catch(error => console.error('Error:', error));
}