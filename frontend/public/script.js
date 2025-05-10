document.getElementById("scheduleForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const totalEmployees = document.getElementById("totalEmployees").value;
  const generalEmployees = document.getElementById("generalEmployees").value;
  const month = document.getElementById("month").value;
  const year = document.getElementById("year").value;

  try {
    const response = await fetch("http://localhost:8080/api/scheduler/generate", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        totalEmployees: Number(totalEmployees),
        generalEmployees: Number(generalEmployees),
        month: Number(month),
        year: Number(year),
      }),
    });

    const data = await response.json();
    displaySchedule("normalSchedule", data.normalSchedule);
    displaySchedule("generalSchedule", data.generalSchedule);
    // Show the export button after the data is displayed
    document.getElementById("exportBtn").style.display = "inline-block";
  } catch (error) {
    alert("Failed to generate schedule. Is your backend running?");
    console.error(error);
  }
});

function displaySchedule(containerId, schedule) {
  const container = document.getElementById(containerId);
  if (!schedule || schedule.length === 0) {
    container.innerHTML = "<p>No schedule found.</p>";
    return;
  }

  const headers = Object.keys(schedule[0]);
  let html = "<table><tr>";

  headers.forEach((key) => {
    html += `<th>${key}</th>`;
  });
  html += "</tr>";

  schedule.forEach((row) => {
    html += "<tr>";
    headers.forEach((key) => {
      html += `<td>${row[key]}</td>`;
    });
    html += "</tr>";
  });

  html += "</table>";
  container.innerHTML = html;
}

// Export the schedule data to CSV
document.getElementById("exportBtn").addEventListener("click", function () {
  const normalSchedule = document.getElementById("normalSchedule").querySelector("table");
  const generalSchedule = document.getElementById("generalSchedule").querySelector("table");

  if (!normalSchedule || !generalSchedule) {
    alert("Please generate the schedule first.");
    return;
  }

  const normalCsv = tableToCsv(normalSchedule);
  const generalCsv = tableToCsv(generalSchedule);

  const blob = new Blob([normalCsv, "\n\n", generalCsv], { type: "text/csv" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = "shift_schedule.csv";
  link.click();
});

// Convert HTML table to CSV format
function tableToCsv(table) {
  const rows = Array.from(table.rows);
  return rows
    .map((row) => {
      const cells = Array.from(row.cells);
      return cells.map((cell) => `"${cell.textContent.replace(/"/g, '""')}"`).join(",");
    })
    .join("\n");
}
