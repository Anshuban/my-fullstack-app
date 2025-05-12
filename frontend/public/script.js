document.getElementById("scheduleForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const totalEmployees = document.getElementById("totalEmployees").value;
  const generalEmployees = document.getElementById("generalEmployees").value;
  const month = document.getElementById("month").value;
  const year = document.getElementById("year").value;
  const leavesInput = document.getElementById("leaves").value;

  let leaves = {};
  try {
    leaves = JSON.parse(leavesInput);
  } catch (err) {
    alert("Invalid JSON format for leaves. Please check your input.");
    return;
  }

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
        leaves: leaves,
      }),
    });

    const data = await response.json();
    displaySchedule("normalSchedule", data.normalSchedule);
    displaySchedule("generalSchedule", data.generalSchedule);
    document.getElementById("exportBtn").style.display = "inline-block";
  } catch (error) {
    alert("Failed to generate schedule. Is your backend running?");
    console.error(error);
  }
});
