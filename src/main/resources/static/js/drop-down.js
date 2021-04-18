let premiseFData = {
    "Wood workshop": ["Leonardo"],
    "Metal workshop": ["Tesla"],
    "Digital production workshop": ["Lumiere"],
    "Prototyping space": ["Bell"],
    "Computers, Multimedia, Printers": ["Monnet", "Blueprinttion", "STEM&Art", "Carnegie"]
}

window.onload = function () {

    let equipmentSelect = document.getElementById("inputNeededEquipment")
    let labSelect = document.getElementById("inputPremise")

    for (let x in labFData) {
        equipmentSelect.options[equipmentSelect.options.length] = new Option(x, x)
    }
    equipmentSelect.onchange = function () {
        labSelect.options.length = 0
        let allPremises = labFData[this.value]

        for (let y in allPremises) {

            labSelect.options[labSelect.options.length] = new Option(allPremises[y], allPremises[y])


        }
    }
}
