#!/bin/bash
set -e

# ============================================
# CONFIGURATION - FILL IN YOUR VALUES
# ============================================
RESOURCE_GROUP="SpingBootApp"           # Your existing resource group
ACR_NAME="springmoneytracker"                       # Your Azure Container Registry name (or create one)
WEB_APP_NAME="expense-tracker-apis"               # Your existing App Service name


# ============================================
# Step 1: Login to Azure (if not already)
# ============================================
echo "🔐 Checking Azure login..."
az account show || az login

# ============================================
# Step 2: Create ACR if you don't have one
# ============================================
# Uncomment these lines if you need to create an ACR:
# echo "🐳 Creating Container Registry..."
# az acr create \
#   --resource-group $RESOURCE_GROUP \
#   --name $ACR_NAME \
#   --sku Basic \
#   --admin-enabled true

# ============================================
# Step 3: Build and Push Docker Image to ACR
# ============================================
echo "🔨 Getting ACR login server..."
ACR_LOGIN_SERVER=$(az acr show --name $ACR_NAME --query loginServer --output tsv)

echo "🔐 Logging into ACR..."
az acr login --name $ACR_NAME

echo "🔨 Building Docker image for AMD64 (Azure architecture)..."
docker build --platform linux/amd64 -t $ACR_LOGIN_SERVER/expense-money-tracker:latest .

echo "📤 Pushing image to ACR..."
az acr update -n springmoneytracker --admin-enabled true
docker push $ACR_LOGIN_SERVER/expense-money-tracker:latest

echo "✅ Image pushed to: $ACR_LOGIN_SERVER/expense-money-tracker:latest"

# ============================================
# Step 4: Configure App Service to use ACR image
# ============================================
echo "🔐 Configuring App Service to pull from ACR..."
ACR_USERNAME=$(az acr credential show --name $ACR_NAME --query username --output tsv)
ACR_PASSWORD=$(az acr credential show --name $ACR_NAME --query "passwords[0].value" --output tsv)

az webapp config container set \
  --name $WEB_APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --docker-registry-server-url https://$ACR_LOGIN_SERVER \
  --docker-registry-server-user $ACR_USERNAME \
  --docker-registry-server-password $ACR_PASSWORD \
  --docker-custom-image-name $ACR_LOGIN_SERVER/expense-money-tracker:latest

# ============================================
# Step 5: Set Environment Variables
# ============================================
echo "⚙️ Setting environment variables..."
az webapp config appsettings set \
  --name $WEB_APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --settings \
    SPRING_PROFILES_ACTIVE="prod" \
    WEBSITES_PORT=8080

# ============================================
# Step 6: Restart the App
# ============================================
echo "🔄 Restarting App Service..."
az webapp restart --name $WEB_APP_NAME --resource-group $RESOURCE_GROUP

# ============================================
# Done!
# ============================================
echo ""
echo "✅ Deployment complete!"
echo ""
echo "🌐 Your app: https://$WEB_APP_NAME.azurewebsites.net"
echo ""
echo "📋 View logs:"
echo "   az webapp log tail --name $WEB_APP_NAME --resource-group $RESOURCE_GROUP"
