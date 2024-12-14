import React, { useEffect, useState } from "react";
import { getFirestore, collection, getDocs, updateDoc, doc } from "firebase/firestore";
import "../App.css"; // Votre fichier CSS

const ClaimsList = () => {
  const [claims, setClaims] = useState([]);
  const db = getFirestore();

  // Récupération des réclamations depuis Firebase
  useEffect(() => {
    const fetchClaims = async () => {
      const claimsCollection = collection(db, "claims");
      const claimsSnapshot = await getDocs(claimsCollection);
      const claimsList = claimsSnapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
      }));
      setClaims(claimsList);
    };

    fetchClaims();
  }, [db]);

  // Fonction pour mettre à jour le statut
  const handleStatusUpdate = async (id, status) => {
    const claimRef = doc(db, "claims", id);
    await updateDoc(claimRef, { status });
    setClaims(
      claims.map((claim) =>
        claim.id === id ? { ...claim, status } : claim
      )
    );
  };

  return (
    <div className="claims-container">
      <h2>Liste des Réclamations</h2>
      {claims.map((claim) => (
        <div
          key={claim.id}
          className={`claim-block ${
            claim.status === "accepté"
              ? "accepted"
              : claim.status === "refusé"
              ? "refused"
              : ""
          }`}
        >
          <h3 style={{ fontWeight: "bold" }}>{claim.teacherEmail}</h3>
          <p>Email : {claim.teacherEmail}</p>
          <p>Contenu : {claim.claimContent}</p>
          <div className="buttons">
            <button
              className="accept-button"
              onClick={() => handleStatusUpdate(claim.id, "accepté")}
            >
              Accepté
            </button>
            <button
              className="refuse-button"
              onClick={() => handleStatusUpdate(claim.id, "refusé")}
            >
              Refusé
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default ClaimsList;
