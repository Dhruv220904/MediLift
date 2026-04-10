"use client";

import { useEffect, useMemo, useState } from "react";

type Stats = {
  total_visits_today: number;
  high_risk_today: number;
  total_beneficiaries: number;
  active_ashas: number;
};

type HighRiskVisit = {
  id: string;
  beneficiary_name: string;
  district?: string | null;
  village?: string | null;
  specialist_flag?: string | null;
  is_referred: boolean;
  assigned_doctor_id?: string | null;
  risk_score: number;
  created_at: string;
};

const API = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8000";

export default function DashboardPage() {
  const [stats, setStats] = useState<Stats | null>(null);
  const [rows, setRows] = useState<HighRiskVisit[]>([]);
  const [district, setDistrict] = useState("");
  const [specialist, setSpecialist] = useState("");

  const query = useMemo(() => {
    const params = new URLSearchParams();
    if (district) params.set("district", district);
    if (specialist) params.set("specialist_flag", specialist);
    return params.toString();
  }, [district, specialist]);

  async function load() {
    const [statsRes, highRes] = await Promise.all([
      fetch(`${API}/stats/`, { cache: "no-store" }),
      fetch(`${API}/stats/high-risk${query ? `?${query}` : ""}`, { cache: "no-store" }),
    ]);
    if (!statsRes.ok || !highRes.ok) return;
    setStats(await statsRes.json());
    setRows(await highRes.json());
  }

  async function markReferred(visitId: string) {
    const bearer = window.localStorage.getItem("asha_token");
    if (!bearer) {
      alert("Set localStorage.asha_token with a doctor/admin JWT before marking referrals.");
      return;
    }
    await fetch(`${API}/visit/${visitId}/refer`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${bearer}`,
      },
      body: JSON.stringify({}),
    });
    await load();
  }

  useEffect(() => {
    load();
  }, [query]);

  return (
    <main>
      <h1>Asha 2.0 Health Command Dashboard</h1>
      <p className="sub">Phase 5 admin view for high-risk routing and district monitoring</p>

      <section className="grid stats">
        <article className="card">
          <div className="sub">Visits Today</div>
          <div className="metric">{stats?.total_visits_today ?? "-"}</div>
        </article>
        <article className="card">
          <div className="sub">High Risk Today</div>
          <div className="metric">{stats?.high_risk_today ?? "-"}</div>
        </article>
        <article className="card">
          <div className="sub">Beneficiaries</div>
          <div className="metric">{stats?.total_beneficiaries ?? "-"}</div>
        </article>
        <article className="card">
          <div className="sub">Active ASHAs</div>
          <div className="metric">{stats?.active_ashas ?? "-"}</div>
        </article>
      </section>

      <section className="card" style={{ marginTop: "1rem" }}>
        <div className="controls">
          <input
            value={district}
            onChange={(e) => setDistrict(e.target.value)}
            placeholder="Filter by district"
          />
          <select value={specialist} onChange={(e) => setSpecialist(e.target.value)}>
            <option value="">All specialists</option>
            <option value="Maternal">Maternal</option>
            <option value="TB">TB</option>
            <option value="Nutrition">Nutrition</option>
            <option value="General">General</option>
          </select>
          <a href={`${API}/export`}>
            <button className="secondary" type="button">Export CSV</button>
          </a>
        </div>

        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Beneficiary</th>
                <th>District / Village</th>
                <th>Risk</th>
                <th>Specialist</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => (
                <tr key={row.id}>
                  <td>{row.beneficiary_name}</td>
                  <td>{(row.district || "-") + " / " + (row.village || "-")}</td>
                  <td>
                    <span className="badge high">{Math.round(row.risk_score)}%</span>
                  </td>
                  <td>
                    <span className="badge">{row.specialist_flag || "General"}</span>
                  </td>
                  <td>{row.is_referred ? "Referred" : "Pending"}</td>
                  <td>
                    {row.is_referred ? (
                      "Done"
                    ) : (
                      <button className="warn" type="button" onClick={() => markReferred(row.id)}>
                        Mark Referred
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </main>
  );
}
