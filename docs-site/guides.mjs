// The published, user-facing docs (allowlist). Shared by the sync script and the
// VitePress config so the set and its order are defined exactly once.
//   file  — source markdown under ../docs
//   title — sidebar / nav label
//   group — sidebar section the entry belongs to
export const GUIDES = [
  { file: 'getting-started.md', title: 'Getting Started', group: 'User Guide' },
  { file: 'binding-model.md', title: 'The Binding Model', group: 'User Guide' },
  { file: 'security.md', title: 'Access Control', group: 'User Guide' },
  { file: 'deployment.md', title: 'Deployment', group: 'User Guide' },

  { file: 'openapi.md', title: 'From an OpenAPI Document', group: 'OpenAPI' },

  { file: 'limitations.md', title: 'Not Covered Yet', group: 'Reference' },
];

// Route name for a guide: the file's base name without the .md extension.
// e.g. 'getting-started.md' -> 'getting-started', served at /guides/getting-started.
export function slugFor(file) {
  return file.replace(/^.*\//, '').replace(/\.md$/, '');
}
