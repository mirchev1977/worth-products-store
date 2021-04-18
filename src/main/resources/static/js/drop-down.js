let premiseFData = {
    "Wood workshop": ["Sofia_Lab"],
    "Metal workshop": ["Varna_Factory"],
    "Digital production workshop": ["Partner_Storage"],
    "Prototyping space": ["Abroad"],
    "Computers, Multimedia, Printers": ["Local_Laboratory", "Quality_Review", "STEM&Art", "Waiting_Room"]
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
