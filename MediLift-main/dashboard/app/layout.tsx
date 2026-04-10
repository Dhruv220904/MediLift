import "./globals.css";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Asha 2.0 Dashboard",
  description: "High-risk community health command dashboard",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
