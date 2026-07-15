db = db.getSiblingDB("golazodb");
let matches = db.partidos.find().sort({_id: 1}).toArray();
if (matches.length >= 32) {
    let thirdPlace = matches[30];
    let finalMatch = matches[31];
    
    db.partidos.updateOne({_id: thirdPlace._id}, {$set: {equipo1: "Por confirmar", equipo2: "Por confirmar", ganador: ""}});
    db.partidos.updateOne({_id: finalMatch._id}, {$set: {equipo1: "Por confirmar", equipo2: "Por confirmar", ganador: ""}});
    print("Matches 30 and 31 cleared successfully.");
} else {
    print("Not enough matches found. Found: " + matches.length);
}
