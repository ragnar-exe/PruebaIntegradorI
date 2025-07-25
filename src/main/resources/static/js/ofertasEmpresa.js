// Search and Filter Functionality
document.addEventListener("DOMContentLoaded", function () {
  const searchInput = document.getElementById("searchInput");
  const statusFilter = document.getElementById("statusFilter");
  const sortFilter = document.getElementById("sortFilter");
  const offersGrid = document.getElementById("offersGrid");
  const emptyState = document.getElementById("emptyState");
  const offerCards = document.querySelectorAll(".offer-card");

  // Search functionality
  searchInput.addEventListener("input", function () {
    filterOffers();
  });

  // Status filter
  statusFilter.addEventListener("change", function () {
    filterOffers();
  });

  // Sort functionality
  sortFilter.addEventListener("change", function () {
    sortOffers();
  });

  function filterOffers() {
    const searchTerm = searchInput.value.toLowerCase();
    const statusValue = statusFilter.value;
    let visibleCount = 0;

    offerCards.forEach((card) => {
      const title = card.dataset.title || "";
      const status = card.dataset.status || "";

      const matchesSearch = title.includes(searchTerm) || searchTerm === "";
      const matchesStatus = status === statusValue || statusValue === "";

      if (matchesSearch && matchesStatus) {
        card.style.display = "block";
        card.classList.add("fade-in");
        visibleCount++;
      } else {
        card.style.display = "none";
      }
    });

    // Show/hide empty state
    if (visibleCount === 0) {
      emptyState.classList.remove("d-none");
      offersGrid.style.display = "none";
    } else {
      emptyState.classList.add("d-none");
      offersGrid.style.display = "grid";
    }
  }

  function sortOffers() {
    const sortValue = sortFilter.value;
    const cardsArray = Array.from(offerCards);

    // Add loading state
    offersGrid.classList.add("loading");

    setTimeout(() => {
      cardsArray.sort((a, b) => {
        switch (sortValue) {
          case "title":
            return a.dataset.title.localeCompare(b.dataset.title);
          case "oldest":
            return -1; // Simplified for demo
          case "newest":
          default:
            return 1; // Simplified for demo
        }
      });

      // Re-append sorted cards
      cardsArray.forEach((card) => {
        offersGrid.appendChild(card);
      });

      offersGrid.classList.remove("loading");
    }, 500);
  }

  // Set minimum date for date inputs
  const today = new Date().toISOString().split("T")[0];
  document.getElementById("fechaLimite").setAttribute("min", today);
  document.getElementById("editFechaLimite").setAttribute("min", today);
});

// Animate stats on load
function animateStats() {
  const statsNumbers = document.querySelectorAll(".stats-number");
  statsNumbers.forEach((stat) => {
    const finalValue = parseInt(stat.textContent);
    let currentValue = 0;
    const increment = finalValue / 30;

    const timer = setInterval(() => {
      currentValue += increment;
      if (currentValue >= finalValue) {
        stat.textContent =
          finalValue + (stat.textContent.includes("%") ? "%" : "");
        clearInterval(timer);
      } else {
        stat.textContent =
          Math.floor(currentValue) +
          (stat.textContent.includes("%") ? "%" : "");
      }
    }, 50);
  });
}

// Run animations on load
setTimeout(animateStats, 1000);
