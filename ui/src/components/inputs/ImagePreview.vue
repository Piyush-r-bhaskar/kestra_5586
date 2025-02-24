<template>
    <div class="image-preview-container">
        <img 
            :src="imageUrl" 
            :alt="fileName"
            class="preview-image"
            @error="handleImageError"
            v-if="!error"
        >
        <div v-else class="error-container">
            <el-icon class="error-icon">
                <warning-filled />
            </el-icon>
            <div class="error-message">
                {{ error }}
            </div>
            <el-button type="primary" size="small" @click="retryLoading">
                Retry
            </el-button>
        </div>
    </div>
</template>

<script setup>
    import {ref, watch} from "vue";
    import {useStore} from "vuex";
    import {apiUrl} from "override/utils/route";
    import {WarningFilled} from "@element-plus/icons-vue";

    const store = useStore();
    const error = ref(null);
    const dimensions = ref(null);
    const imageUrl = ref("");

    const props = defineProps({
        filePath: {
            type: String,
            required: true
        },
        fileName: {
            type: String,
            required: true
        },
        fileSize: {
            type: Number,
            default: null
        },
        namespace: {
            type: String,
            required: true
        }
    });

    const handleImageError = () => {
        error.value = "Failed to load image. Please check if the file is a valid image.";
        console.error("Image load error:", {
            filePath: props.filePath,
            fileName: props.fileName,
            namespace: props.namespace,
            url: imageUrl.value
        });
    };

    const loadImage = async () => {
        try {
            error.value = null;
        
            // Get the base API URL
            const baseApiUrl = apiUrl(store);
        
            // Format the path
            const slashPrefix = (path) => (path.startsWith("/") ? path : `/${path}`);
            const safePath = (path) => encodeURIComponent(path).replace(/%2F/g, "/");
        
            // Remove any leading slashes from filePath before adding our own
            const cleanPath = props.filePath.replace(/^\/+/, "");
            const formattedPath = slashPrefix(safePath(cleanPath));
        
            // Construct the file URL
            const fileUrl = `${baseApiUrl}/namespaces/${props.namespace}/files?path=${formattedPath}`;
            imageUrl.value = fileUrl;
        
            // Create an image object to get dimensions
            const img = new Image();
            img.onload = () => {
                dimensions.value = {
                    width: img.naturalWidth,
                    height: img.naturalHeight
                };
                error.value = null;
            };
            img.onerror = handleImageError;
            img.src = fileUrl;
        } catch (e) {
            console.error("Error loading image:", e);
            handleImageError();
        }
    };

    const retryLoading = () => {
        loadImage();
    };

    // Watch for changes in props and reload the image
    watch([() => props.filePath, () => props.namespace], () => {
        loadImage();
    }, {immediate: true});

</script>

<style lang="scss" scoped>
.image-preview-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    background: var(--ks-background-body);
    padding: 20px;

    .preview-image {
        max-width: 100%;
        max-height: calc(100vh - 200px);
        object-fit: contain;
        margin-bottom: 16px;
    }

    .error-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 12px;
        padding: 24px;
        text-align: center;

        .error-icon {
            font-size: 32px;
            color: #f56c6c;
        }

        .error-message {
            color: #666;
            margin-bottom: 8px;
        }
    }

    .image-info {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 4px;
        color: #666;
        font-size: 14px;

        .file-name {
            font-weight: 500;
            color: #333;
        }

        .file-size, .dimensions {
            font-size: 12px;
        }
    }
}
</style>
