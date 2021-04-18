const productList = document.getElementById('productList');
const searchBar = document.getElementById('searchInput');
const allBtn = document.getElementById('all');
const myProductBtn = document.getElementById('own');
const collabsBtn = document.getElementById('collabs');
const allProducts = [];

fetch("https://wprod.herokuapp.com/manageProducts/api/all")
    .then(response => response.json())
    .then(data => {
        for (let d of data) {
            allProducts.push(d);
        }
    });

const myProducts = [];
fetch("https://wprod.herokuapp.com/manageProducts/own")
    .then(response => response.json())
    .then(data => {
        for (let d of data) {
            myProducts.push(d);
        }
    });

const myCollabProducts = [];
fetch("https://wprod.herokuapp.com/manageProducts/collaborations")
    .then(response => response.json())
    .then(data => {
        for (let d of data) {
            myCollabProducts.push(d);
        }
    });

// Start of functions

allBtn.addEventListener('click', (e) => {
    productList.innerHTML = ""
    displayProducts(allProducts);

})

searchBar.addEventListener('keyup', (e) => {
    const searchingCharacters = searchBar.value.toLowerCase();
    let filteredProducts = allProducts.filter(p => {
        return p.name.toLowerCase().includes(searchingCharacters)
            || p.category.toLowerCase().includes(searchingCharacters);
    });
    productList.innerHTML = ""
    displayProducts(filteredProducts);
})

myProductBtn.addEventListener('click', (e) => {
    productList.innerHTML = ""
    displayProducts(myProducts);
})

collabsBtn.addEventListener('click', (e) => {
    productList.innerHTML = ""
    displayProducts(myCollabProducts);
})

function displayProducts(products) {
    productList.innerHTML = products
        .map((p) => {
            return `    <tr>
                <td >${p.name}</td>
                <td >${p.category}</td>
                <td>${p.startDate}</td>
                <td>
                    <a href="/products/details/${p.id}">
                        <button type="button">Details</button></a>
             
                </td>
            </tr> `
        })
        .join('');

}
