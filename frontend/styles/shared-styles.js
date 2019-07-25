import '@polymer/polymer/lib/elements/custom-style.js';
const documentContainer = document.createElement('template');

documentContainer.innerHTML = `
<dom-module id="tree-grid-theme" theme-for="vaadin-grid-tree-toggle">
    <template>
        <style>
            :host(vaadin-grid-tree-toggle) {
                --vaadin-grid-tree-toggle-level-offset: 0.75em;
            }
        </style>
    </template>
</dom-module>`;

document.head.appendChild(documentContainer.content);
