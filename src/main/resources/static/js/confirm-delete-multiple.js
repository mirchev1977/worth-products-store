const deleteBtns = Array.from(document.getElementsByClassName('bg-danger'))

deleteBtns.forEach(b => b.addEventListener('click', (e) => {
    let confirmation = confirm("The action will delete all blueprints submitted by this user and all products created from these blueprints. Proceed anyway?")
    if (confirmation === false) {
        e.preventDefault();
    }
}))

