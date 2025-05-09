document.getElementById("scheduleForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const totalEmployees = document.getElementById("totalEmployees").value;
  const generalEmployees = document.getElementById("generalEmployees").value;

  try {
    const response = await fetch("http://localhost:8080/api/scheduler/generate", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        totalEmployees: Number(totalEmployees),
        generalEmployees: Number(generalEmployees),
      }),
    });

    const data = await response.json();
    displaySchedule("normalSchedule", data.normalSchedule);
    displaySchedule("generalSchedule", data.generalSchedule);
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
