<div id="editor-container"></div>

<form id="badge-editor-save-form" action="{{ site.sitePath }}/groups/actions/update_group_badge" method="post" style="display:none">
    <input type="hidden" name="groupId" id="badge-editor-group-id" value="{{ group.getId() }}" />
    <input type="hidden" name="code" id="badge-editor-code" value="" />
</form>

<script type="text/javascript">
window.HabboBadgeEditorConfig = {
    badge_data: "{{ group.getBadge() }}",
    assetsPath: "{{ site.staticContentPath }}/badge-editor/",
    assetBundlePath: "assets.zip",
    badge_data_url: "data/badge_data.xml",
    localization_url: "data/badge_editor.xml",
    groupId: "{{ group.getId() }}"
};

window.HabboBadgeEditor = {
    onSave: function(code, groupId) {
        document.getElementById("badge-editor-group-id").value = groupId;
        document.getElementById("badge-editor-code").value = code;
        document.getElementById("badge-editor-save-form").submit();
    },
    onCancel: function() {
        window.location.href = "{{ group.generateClickLink() }}";
    }
};
</script>
<script src="{{ site.staticContentPath }}/badge-editor/badge-editor.js" type="text/javascript"></script>
