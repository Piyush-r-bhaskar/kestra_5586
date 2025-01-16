let highlighter = null;

async function getHighlighter(createHighlighterCore, githubDark, githubLight) {
    if (!highlighter) {
        highlighter = createHighlighterCore({
            langs: [import("shiki/langs/yaml.mjs"), import("shiki/langs/python.mjs"), import("shiki/langs/javascript.mjs")],
            themes: [githubDark, githubLight],
            loadWasm: import("shiki/wasm"),
        });
    }
    return highlighter;
}

export async function render(markdown, options = {}) {
    const {createHighlighterCore, githubDark, githubLight, markdownIt, mark, meta, anchor, container, fromHighlighter, linkTag} = await import( "./markdownDeps")
    const highlighter = await getHighlighter(createHighlighterCore, githubDark, githubLight);

    githubDark["colors"]["editor.background"] = "var(--bs-gray-500)";
    githubLight["colors"]["editor.background"] = "var(--bs-white)";

    const darkTheme = document.getElementsByTagName("html")[0].className.indexOf("dark") >= 0;

    let md;
    if (options.onlyLink) {
        md = new markdownIt("zero");
        md.enable(["link", "linkify", "entity", "html_inline", "newline"]);
    } else {
        md = new markdownIt();
    }

    md.use(mark)
        .use(meta)
        .use(anchor, {permalink: options.permalink ? anchor.permalink.ariaHidden({placement: "before"}) : undefined})
        .use(container, "warning")
        .use(container, "info")
        .use(fromHighlighter(highlighter, {theme: darkTheme ? "github-dark" : "github-light"}))
        .use(linkTag);

    md.set({
        html: true,
        xhtmlOut: true,
        breaks: true,
        linkify: true,
        typographer: true,
        langPrefix: "language-",
        quotes: "“”‘’",
    });

    md.renderer.rules.table_open = () => "<table class=\"table\">\n";

    return md.render(markdown);
}
