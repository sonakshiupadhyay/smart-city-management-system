/**
 * Smart City Management System - Frontend Interactivity & Simulated Telemetry Polling
 */

document.addEventListener('DOMContentLoaded', () => {
    initLiveTelemetryPolling();
    initModalControls();
});

/**
 * Periodically polls the simulated sensor telemetry REST endpoint to update alert feeds and active node counts.
 */
function initLiveTelemetryPolling() {
    const alertBox = document.getElementById('liveAlertsContainer');
    const sensorCountBadge = document.getElementById('liveSensorCount');

    if (!alertBox && !sensorCountBadge) return;

    function fetchLiveData() {
        fetch(contextPath + '/api/live-data')
            .then(res => res.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    if (sensorCountBadge && data.sensors) {
                        const activeCount = data.sensors.filter(s => s.status === 'ACTIVE').length;
                        sensorCountBadge.textContent = activeCount;
                    }
                    if (alertBox && data.liveAlerts) {
                        if (data.liveAlerts.length === 0) {
                            alertBox.innerHTML = '<p class="text-muted">No critical alerts detected in the city grid.</p>';
                        } else {
                            let html = '<ul class="alerts-feed-list" style="list-style:none; display:flex; flex-direction:column; gap:0.5rem;">';
                            data.liveAlerts.slice(0, 5).forEach(alert => {
                                html += '<li style="padding:0.6rem 0.8rem; background:rgba(239,68,68,0.1); border-left:3px solid #ef4444; border-radius:4px; font-size:0.85rem;">' + alert + '</li>';
                            });
                            html += '</ul>';
                            alertBox.innerHTML = html;
                        }
                    }
                }
            })
            .catch(err => {
                console.debug('Telemetry poll pause:', err);
            });
    }

    // Initial poll and recurring poll every 12 seconds
    fetchLiveData();
    setInterval(fetchLiveData, 12000);
}

/**
 * Modal dialog handler for CRUD operations.
 */
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'flex';
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'none';
    }
}

function initModalControls() {
    window.addEventListener('click', (e) => {
        if (e.target.classList.contains('modal-backdrop')) {
            e.target.style.display = 'none';
        }
    });
}
