// Charts initialization
document.addEventListener('DOMContentLoaded', function() {
    // Sales Chart
    const salesCtx = document.getElementById('salesChart').getContext('2d');
    new Chart(salesCtx, {
        type: 'line',
        data: {
            labels: last7Days(),
            datasets: [{
                label: 'Doanh thu',
                data: salesData,
                borderColor: '#EA6C80',
                tension: 0.4,
                fill: true,
                backgroundColor: 'rgba(234, 108, 128, 0.1)'
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Biểu đồ doanh thu 7 ngày gần nhất'
                }
            }
        }
    });

    // Orders by Status Chart
    const ordersCtx = document.getElementById('ordersChart').getContext('2d');
    new Chart(ordersCtx, {
        type: 'doughnut',
        data: {
            labels: ['Đang xử lý', 'Đã xác nhận', 'Đang giao', 'Đã giao', 'Đã hủy'],
            datasets: [{
                data: orderStatusData,
                backgroundColor: [
                    '#ffc107',
                    '#0dcaf0',
                    '#0d6efd',
                    '#198754',
                    '#dc3545'
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom'
                },
                title: {
                    display: true,
                    text: 'Thống kê trạng thái đơn hàng'
                }
            }
        }
    });
});

// Generate last 7 days for chart labels
function last7Days() {
    const result = [];
    for (let i = 6; i >= 0; i--) {
        const d = new Date();
        d.setDate(d.getDate() - i);
        result.push(d.toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit' }));
    }
    return result;
}

// Toggle sidebar
document.getElementById('sidebarCollapse').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('active');
});

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

// Format date
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}
